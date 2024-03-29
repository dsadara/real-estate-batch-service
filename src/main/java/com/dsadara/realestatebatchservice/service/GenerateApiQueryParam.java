package com.dsadara.realestatebatchservice.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Getter
public class GenerateApiQueryParam {

    private Map<String, String> bjdCodeMap;
    private List<String> dealYearMonthsList;
    private List<String> bjdCodeList;

    public GenerateApiQueryParam() throws FileNotFoundException {
        dealYearMonthsList = new LinkedList<>();
        bjdCodeMap = new LinkedHashMap<>();
        generateDealYearMonth();
        parseBjdCodeToMap();
        bjdCodeList = new LinkedList<>(bjdCodeMap.keySet());
    }

    private void generateDealYearMonth() {
        Period period = Period.between(LocalDate.of(2005, 1, 1), LocalDate.now());
        int periodMonths = period.getYears() * 12 + period.getMonths();
        LocalDate startDate = LocalDate.of(2005, 1, 1);
        dealYearMonthsList.add(String.format("%d%02d", startDate.getYear(), startDate.getMonthValue()));
        for (int i = 0; i < periodMonths; i++) {
            startDate = startDate.plusMonths(1);
            dealYearMonthsList.add(String.format("%d%02d", startDate.getYear(), startDate.getMonthValue()));
        }
    }

    private void parseBjdCodeToMap() throws FileNotFoundException {
        String file = "src/main/resources/bjdcode.txt";
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\t");

        String bjdCode;
        String siGunGu;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            bjdCode = scanner.next().substring(0, 5);
            siGunGu = scanner.next();
            if (siGunGu.contains(" ")) {
                String[] s = siGunGu.split(" ");
                siGunGu = s[0] + " " + s[1];
                bjdCodeMap.put(bjdCode, siGunGu);
            }
            scanner.nextLine();
        }
        scanner.close();
    }

}
