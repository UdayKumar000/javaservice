package com.training.web.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.training.business.bean.MaterialCategoryBean;
import com.training.exception.MicroServiceException;

@Service
public class MaterialCategoryConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(MaterialCategoryConsumer.class);

    @Value("${MaterialServiceConsumer.serviceURL}")
    private String serviceURL;

    @Value("${MaterialCategoryConsumer.apiURL}")
    private String apiURL;

    @Value("${MaterialCategoryConsumer.apiURLForById}")
    private String apiURLForById;

    private List<MaterialCategoryBean> materialCategoryBeanList;

    private Map<String, MaterialCategoryBean> materialCategoryMap;

    private RestTemplate restTemplate;

    public MaterialCategoryConsumer() {
        restTemplate = new RestTemplate();
    }

    public List<MaterialCategoryBean> getMaterialCategoryBeanList()
            throws MicroServiceException {

        if (materialCategoryBeanList == null) {
            hitGetMaterialCategoryDetails();
        }

        return materialCategoryBeanList;
    }

    public Map<String, MaterialCategoryBean> getMaterialCategoryMap()
            throws MicroServiceException {

        if (materialCategoryMap == null) {

            materialCategoryMap = new HashMap<String, MaterialCategoryBean>();

            for (MaterialCategoryBean bean : materialCategoryBeanList) {

                materialCategoryMap.put(bean.getCategoryId(), bean);
            }
        }

        return materialCategoryMap;
    }

    private void hitGetMaterialCategoryDetails()
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetMaterialCategoryDetails()]");

        try {

            MaterialCategoryBean[] response = restTemplate.getForObject(
                    serviceURL + apiURL,
                    MaterialCategoryBean[].class);

            List<MaterialCategoryBean> categoryList = Arrays.asList(response);

            LOGGER.info("Execution Over [hitGetMaterialCategoryDetails()]");

            this.materialCategoryBeanList = categoryList;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }
    }

    public MaterialCategoryBean hitGetMaterialCategoryById(String categoryId)
            throws MicroServiceException {

        LOGGER.info("Execution Started [hitGetMaterialCategoryById()]");

        try {

            MaterialCategoryBean response = restTemplate.getForObject(
                    serviceURL + apiURLForById + "/" + categoryId,
                    MaterialCategoryBean.class);

            LOGGER.info("Execution Over [hitGetMaterialCategoryById()]");

            return response;

        } catch (Exception exception) {

            throw new MicroServiceException();
        }
    }

}