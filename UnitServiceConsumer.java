package com.training.web.client;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.training.business.bean.UnitBean;
import com.training.exception.MicroServiceException;

@Service
public class UnitServiceConsumer {

    private static Logger LOGGER =
            LoggerFactory.getLogger(UnitServiceConsumer.class);

    @Value("${MaterialServiceConsumer.serviceURL}")
    private String serviceURL;

    @Value("${UnitServiceConsumer.apiURL}")
    private String apiURL;

    @Value("${UnitServiceConsumer.apiURLByCategoryId}")
    private String apiURLByCategoryId;

    private List<UnitBean> unitBeanList;

    private RestTemplate restTemplate;

    public UnitServiceConsumer() {
        restTemplate = new RestTemplate();
    }

    public List<UnitBean> getUnitBeanList()
            throws MicroServiceException {

        if (unitBeanList == null) {
            hitGetUnitDetails();
        }

        return unitBeanList;
    }

    private void hitGetUnitDetails()
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetUnitDetails()]");

        try {

            UnitBean[] response =
                    restTemplate.getForObject(
                            serviceURL + apiURL,
                            UnitBean[].class);

            List<UnitBean> unitList =
                    Arrays.asList(response);

            LOGGER.info("Execution Over [hitGetUnitDetails()]");

            this.unitBeanList = unitList;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }

    }

    public List<UnitBean> hitGetUnitsByCategoryId(String categoryId)
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetUnitsByCategoryId()]");

        try {

            UnitBean[] response =
                    restTemplate.getForObject(
                            serviceURL + apiURLByCategoryId + categoryId,
                            UnitBean[].class);

            List<UnitBean> unitList =
                    Arrays.asList(response);

            LOGGER.info("Execution Over [hitGetUnitsByCategoryId()]");

            return unitList;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }

    }

}