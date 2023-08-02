package com.dsadara.realestatebatchservice.service;

import com.dsadara.realestatebatchservice.dto.RealEstateDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestDataTest {
    @Mock
    private JsonDeserializer jsonDeserializer;

    @InjectMocks
    private RequestData requestData;

    // 아파트 전월세 요청 파라미터, 비교용 데이터
    private static JsonNode jsonNodeSample;
    private static List<RealEstateDataDto> realEstateDataDtos;
    private static ResponseEntity<String> responseSample;
    private static String baseURL;
    private static String legalDong;
    private static String contractYMD;
    private static String searchKey;

    @BeforeAll
    static void beforeAll() throws Exception {
        prepareDataForRequestAptRent();
    }

    static void prepareDataForRequestAptRent() throws Exception {
        JsonDeserializer jsonDeserializerTemp = new JsonDeserializer(new ObjectMapper(), new RestTemplate());

        legalDong = "11500";
        contractYMD = "202304";
        searchKey = "KNxUoxDnwzkyp3fb8dOjCWatfWm6VdGxJHzwOlvkSAcOcm%2B6%2BgIsOrcZ8Wr8hU0qzcmNE2tSjG7HUQBIA%2FqkYg%3D%3D";
        baseURL = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptRent?";

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("LAWD_CD", legalDong);
        queryParams.add("DEAL_YMD", contractYMD);
        queryParams.add("serviceKey", searchKey);

        responseSample = jsonDeserializerTemp.getResponse(baseURL, queryParams);
        jsonNodeSample = jsonDeserializerTemp.stringToJsonNode(responseSample.getBody());
        realEstateDataDtos = jsonDeserializerTemp.jsonNodeToPOJO(jsonNodeSample);
    }

    @Test
    @DisplayName("성공-requestData()")
    void requestData_Success() throws Exception {
        //given
        //when
        when(jsonDeserializer.getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any())).thenReturn(responseSample);
        when(jsonDeserializer.stringToJsonNode(anyString())).thenReturn(jsonNodeSample);
        when(jsonDeserializer.jsonNodeToPOJO(any(JsonNode.class))).thenReturn(realEstateDataDtos);
        //then
        Assertions.assertEquals(realEstateDataDtos, requestData.requestData(baseURL, legalDong, contractYMD, searchKey));
        verify(jsonDeserializer, times(1)).getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any());
        verify(jsonDeserializer, times(1)).stringToJsonNode(anyString());
        verify(jsonDeserializer, times(1)).jsonNodeToPOJO(any(JsonNode.class));
    }

    @Test
    @DisplayName("실패-requestData()-IOException")
    void requestData_Failure_IOException() throws Exception {
        //given
        //when
        when(jsonDeserializer.getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any())).thenReturn(responseSample);
        when(jsonDeserializer.stringToJsonNode(anyString())).thenReturn(jsonNodeSample);
        when(jsonDeserializer.jsonNodeToPOJO(any(JsonNode.class))).thenThrow(IOException.class);
        //then
        Assertions.assertThrows(IOException.class, () -> requestData.requestData(baseURL, legalDong, contractYMD, searchKey));
        verify(jsonDeserializer, times(1)).getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any());
        verify(jsonDeserializer, times(1)).stringToJsonNode(anyString());
    }

    @Test
    @DisplayName("실패-requestData()-JsonProcessionException")
    void requestAptRent_Failure_JsonProcessionException() throws Exception {
        //given
        //when
        when(jsonDeserializer.getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any())).thenReturn(responseSample);
        when(jsonDeserializer.stringToJsonNode(anyString())).thenReturn(jsonNodeSample);
        when(jsonDeserializer.jsonNodeToPOJO(any(JsonNode.class))).thenThrow(JsonProcessingException.class);
        //then
        Assertions.assertThrows(JsonProcessingException.class, () -> requestData.requestData(baseURL, legalDong, contractYMD, searchKey));
        verify(jsonDeserializer, times(1)).getResponse(anyString(), ArgumentMatchers.<MultiValueMap<String, String>>any());
    }
}