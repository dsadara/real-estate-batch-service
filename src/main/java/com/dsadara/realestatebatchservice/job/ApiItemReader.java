package com.dsadara.realestatebatchservice.job;

import com.dsadara.realestatebatchservice.dto.RealEstateDto;
import com.dsadara.realestatebatchservice.service.GenerateApiQueryParam;
import com.dsadara.realestatebatchservice.service.RequestDataAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ApiItemReader implements ItemReader<RealEstateDto> {
    private final String baseUrl;
    private final String serviceKey;
    private final RequestDataAsync requestDataAsync;
    private final List<RealEstateDto> items;
    private final List<String> BjdCodeList;
    private int currentBjdCode;

    public ApiItemReader(String baseUrl, String serviceKey, RequestDataAsync requestDataAsync, GenerateApiQueryParam generateApiQueryParam) {
        this.baseUrl = baseUrl;
        this.serviceKey = serviceKey;
        this.requestDataAsync = requestDataAsync;
        BjdCodeList = generateApiQueryParam.getBjdCodeList();
        items = new LinkedList<>();
        currentBjdCode = 0;
    }

    @Override
    public RealEstateDto read() throws Exception {
        if (items.isEmpty() && currentBjdCode < BjdCodeList.size()) {
            log.info("법정동 코드 {} 요청", BjdCodeList.get(currentBjdCode));
            items.addAll(requestDataAsync.requestDataOneBjd(baseUrl, BjdCodeList.get(currentBjdCode), serviceKey));
            currentBjdCode++;
        }

        if (items.isEmpty()) {
            return null;
        } else {
            return items.remove(0);
        }
    }
}
