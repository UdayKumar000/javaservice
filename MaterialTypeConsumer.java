package com.training.web.client;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.training.business.bean.MaterialTypeBean;
import com.training.exception.MicroServiceException;

@Service
public class MaterialTypeConsumer {

    private static Logger LOGGER =
            LoggerFactory.getLogger(MaterialTypeConsumer.class);

    @Value("${MaterialServiceConsumer.serviceURL}")
    private String serviceURL;

    @Value("${MaterialTypeConsumer.apiURL}")
    private String apiURL;

    @Value("${MaterialTypeConsumer.apiURLByCategoryId}")
    private String apiURLByCategoryId;

    private List<MaterialTypeBean> materialTypeBeanList;

    private RestTemplate restTemplate;

    public MaterialTypeConsumer() {
        restTemplate = new RestTemplate();
    }

    public List<MaterialTypeBean> getMaterialTypeBeanList()
            throws MicroServiceException {

        if (materialTypeBeanList == null) {
            hitGetTypeDetails();
        }

        return materialTypeBeanList;
    }

    private void hitGetTypeDetails()
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetTypeDetails()]");

        try {

            MaterialTypeBean[] response =
                    restTemplate.getForObject(
                            serviceURL + apiURL,
                            MaterialTypeBean[].class);

            List<MaterialTypeBean> typeList =
                    Arrays.asList(response);

            LOGGER.info("Execution Over [hitGetTypeDetails()]");

            this.materialTypeBeanList = typeList;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }

    }

    public List<MaterialTypeBean> hitGetTypesBasedOnCategoryId(String categoryId)
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetTypesBasedOnCategoryId()]");

        try {

            MaterialTypeBean[] response =
                    restTemplate.getForObject(
                            serviceURL + apiURLByCategoryId + categoryId,
                            MaterialTypeBean[].class);

            List<MaterialTypeBean> typeList =
                    Arrays.asList(response);

            LOGGER.info("Execution Over [hitGetTypesBasedOnCategoryId()]");

            return typeList;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }

    }

}