public class PurchaseEntryControllerTest {

}
package com.training.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.business.bean.MaterialCategoryBean;
import com.training.business.bean.MaterialTypeBean;
import com.training.business.bean.PurchaseBean;
import com.training.business.service.PurchaseService;
import com.training.exception.GlobalExceptionHandler;
import com.training.exception.MicroServiceException;
import com.training.exception.PurchaseException;
import com.training.web.client.MaterialCategoryConsumer;
import com.training.web.client.MaterialTypeConsumer;
import com.training.web.client.UnitServiceConsumer;
import com.training.web.client.VendorServiceConsumer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class PurchaseEntryControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private MaterialCategoryConsumer materialCategoryConsumer;

    @Mock
    private MaterialTypeConsumer materialTypeConsumer;

    @Mock
    private UnitServiceConsumer unitServiceConsumer;

    @Mock
    private VendorServiceConsumer vendorServiceConsumer;

    @InjectMocks
    private PurchaseEntryController purchaseEntryController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(purchaseEntryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private PurchaseBean createPurchaseBean() {

        PurchaseBean bean = new PurchaseBean();

        bean.setPurchaseId(1);
        bean.setTransactionId("TXN1001");
        bean.setVendorName("ABC Vendor");
        bean.setMaterialCategoryId("MC01");
        bean.setMaterialTypeId("MT01");
        bean.setBrandName("Samsung");
        bean.setUnitId("KG");
        bean.setQuantity(10);
        bean.setPurchaseAmount(2500.0);
        bean.setPurchaseDate(new Date());
        bean.setStatus("SUCCESS");

        return bean;
    }

    @Test
    void testSavePurchaseSuccess() throws Exception {

        PurchaseBean bean = createPurchaseBean();

        when(purchaseService.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(bean);

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bean)))
                .andExpect(status().isOk());
    }

    @Test
    void testSavePurchaseException() throws Exception {

        PurchaseBean bean = createPurchaseBean();

        when(purchaseService.savePurchaseDetail(any(PurchaseBean.class)))
                .thenThrow(new PurchaseException());

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bean)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetMaterialCategories() throws Exception {

        MaterialCategoryBean bean = new MaterialCategoryBean("MC01", "Electrical");

        when(materialCategoryConsumer.getMaterialCategoryBeanList())
                .thenReturn(Arrays.asList(bean));

        mockMvc.perform(get("/purchase/controller/getMaterialCategories"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMaterialCategoriesException() throws Exception {

        when(materialCategoryConsumer.getMaterialCategoryBeanList())
                .thenThrow(new MicroServiceException());

        mockMvc.perform(get("/purchase/controller/getMaterialCategories"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetMaterialTypesByCategoryIdSuccess() throws Exception {

        MaterialTypeBean bean = new MaterialTypeBean("MT01", "Wire", "MC01");

        when(materialTypeConsumer.hitGetTypesBasedOnCategoryId("MC01"))
                .thenReturn(Arrays.asList(bean));

        mockMvc.perform(get("/purchase/controller/getMaterialTypes/MC01"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMaterialTypesByCategoryIdException() throws Exception {

        when(materialTypeConsumer.hitGetTypesBasedOnCategoryId("MC01"))
                .thenThrow(new MicroServiceException());

        mockMvc.perform(get("/purchase/controller/getMaterialTypes/MC01"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetUnitsByCategoryIdSuccess() throws Exception {

        UnitBean bean = new UnitBean("U01", "Kilogram");

        when(unitServiceConsumer.hitGetUnitsByCategoryId("MC01"))
                .thenReturn(Arrays.asList(bean));

        mockMvc.perform(get("/purchase/controller/getUnits/MC01"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUnitsByCategoryIdException() throws Exception {

        when(unitServiceConsumer.hitGetUnitsByCategoryId("MC01"))
                .thenThrow(new MicroServiceException());

        mockMvc.perform(get("/purchase/controller/getUnits/MC01"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetVendorsSuccess() throws Exception {

        VendorBean bean = new VendorBean(
                "V001",
                "ABC Vendor",
                "Pune",
                "Rahul",
                "9876543210");

        when(vendorServiceConsumer.getVendorBeanList())
                .thenReturn(Arrays.asList(bean));

        mockMvc.perform(get("/purchase/controller/getVendors"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVendorsException() throws Exception {

        when(vendorServiceConsumer.getVendorBeanList())
                .thenThrow(new MicroServiceException());

        mockMvc.perform(get("/purchase/controller/getVendors"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSavePurchaseValidationFailure() throws Exception {

        PurchaseBean bean = new PurchaseBean();

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bean)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSavePurchaseEmptyBody() throws Exception {

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSavePurchaseInvalidJson() throws Exception {

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetMaterialTypesEmptyList() throws Exception {

        when(materialTypeConsumer.hitGetTypesBasedOnCategoryId("MC01"))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/purchase/controller/getMaterialTypes/MC01"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUnitsEmptyList() throws Exception {

        when(unitServiceConsumer.hitGetUnitsByCategoryId("MC01"))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/purchase/controller/getUnits/MC01"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVendorsEmptyList() throws Exception {

        when(vendorServiceConsumer.getVendorBeanList())
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/purchase/controller/getVendors"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMaterialCategoriesEmptyList() throws Exception {

        when(materialCategoryConsumer.getMaterialCategoryBeanList())
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/purchase/controller/getMaterialCategories"))
                .andExpect(status().isOk());
    }

    @Test
    void testSavePurchaseAnotherSuccess() throws Exception {

        PurchaseBean bean = createPurchaseBean();
        bean.setVendorName("Vendor XYZ");
        bean.setQuantity(20);
        bean.setPurchaseAmount(5000.0);

        when(purchaseService.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(bean);

        mockMvc.perform(post("/purchase/controller/savePurchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bean)))
                .andExpect(status().isOk());
    }

@Test
void testVerifyPurchaseServiceCalled() throws Exception {

    PurchaseBean bean = createPurchaseBean();

    when(purchaseService.savePurchaseDetail(any(PurchaseBean.class)))
            .thenReturn(bean);

    mockMvc.perform(post("/purchase/controller/savePurchase")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bean)))
            .andExpect(status().isOk());

    verify(purchaseService, times(1))
            .savePurchaseDetail(any(PurchaseBean.class));
}