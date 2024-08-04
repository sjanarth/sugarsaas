package com.sugarsaas.api.loader;

import com.sugarsaas.api.refdata.CountrySubDivision;
import com.sugarsaas.api.refdata.CountrySubDivisionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Order(101)
@Component
public class RefDataCountrySubDivisionLoader implements CommandLineRunner
{
    @Autowired
    private CountrySubDivisionRepository subDivisionRepository;

    @Override
    public void run(String... args) throws Exception {
        /*
        log.info("Processing CountrySubDivisions reference data");
        final String FROM_RESOURCE = "data/ref_subdivs.csv";
        final String FROM_THE_WEB = "https://gist.github.com/planemad/73fb92db731a9f2004b0e1ec50776b4b#file-iso_3166-2_subdivision_codes-csv";
        URL url = getClass().getClassLoader().getResource(FROM_RESOURCE);
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            int count = 1;
            String record = br.readLine();
            while (record != null)  {
                load(parse(record));
                count++;
                if ((count%100) == 0)   {
                    log.info("  Processed {} records", count);
                }
                record = br.readLine();
            }
        }
         */
    }

    public Optional<CountrySubDivision> parse(String record) {
        if (record != null) {
            List<String> isoCountries = Arrays.asList(Locale.getISOCountries());
            String[] splits = record.split("\"");
            if (splits.length >= 8) {
                String countryCode = splits[1].replace("\"","").trim();
                String subdivCode = splits[3].replace("\"","").trim();
                String subdivName = splits[5].replace("\"","").split(",")[0].split("\\(")[0].trim();
                String subdivType = splits[7].replace("\"","").trim();
                if (isoCountries.contains(countryCode) && subdivName.length() <= 64 && subdivType.length() <= 32) {
                    return Optional.of(new CountrySubDivision(countryCode, subdivCode, subdivName, subdivType));
                }
            }
        }
        log.warn("  Unable to map {}", record);
        return Optional.empty();
    }

    public void load(Optional<CountrySubDivision> oSubdiv) {
        if (oSubdiv.isPresent()) {
            CountrySubDivision subdiv = oSubdiv.get();
            Optional<CountrySubDivision> oSubdiv2 = subDivisionRepository.findByCountryCodeAndSubdivCode(subdiv.getCountryCode(), subdiv.getSubdivCode());
            if (oSubdiv2.isPresent()) {
                CountrySubDivision subdiv2 = oSubdiv2.get();
                if (!subdiv.getSubdivName().equals(subdiv2.getSubdivName()) ||
                    !subdiv.getSubdivType().equals(subdiv2.getSubdivType()))    {
                    log.debug("  Updating country subdivison {}", subdiv);
                    subdiv2.setSubdivName(subdiv.getSubdivName());
                    subdiv2.setSubdivType(subdiv.getSubdivType());
                    subDivisionRepository.saveAndFlush(subdiv2);
                }
            } else {
                log.debug("  Loading country subdivison {}", subdiv);
                CountrySubDivision subdiv2 = new CountrySubDivision();
                subdiv2.setCountryCode(subdiv.getCountryCode());
                subdiv2.setSubdivCode(subdiv.getSubdivCode());
                subdiv2.setSubdivName(subdiv.getSubdivName());
                subdiv2.setSubdivType(subdiv.getSubdivType());
                subDivisionRepository.saveAndFlush(subdiv2);
            }
        }
    }
}