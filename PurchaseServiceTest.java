package com.training.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.training.business.bean.PurchaseBean;
import com.training.dao.PurchaseDataAccess;
import com.training.exception.PurchaseException;

class PurchaseServiceTest {

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private PurchaseDataAccess purchaseDataAccess;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PurchaseBean createPurchaseBean() {

        PurchaseBean bean = new PurchaseBean();

        bean.setPurchaseId(1);
        bean.setTransactionId("TXN001");
        bean.setVendorName("ABC Vendor");
        bean.setMaterialCategoryId("MC01");
        bean.setMaterialTypeId("MT01");
        bean.setBrandName("Samsung");
        bean.setUnitId("KG");
        bean.setQuantity(10);
        bean.setPurchaseAmount(5000.0);
        bean.setPurchaseDate(new Date());
        bean.setStatus("SUCCESS");

        return bean;
    }

    @Test
    void testSavePurchaseDetailSuccess() throws Exception {

        PurchaseBean bean = createPurchaseBean();

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(bean);

        PurchaseBean response = purchaseService.savePurchaseDetail(bean);

        assertNotNull(response);
        assertEquals("ABC Vendor", response.getVendorName());

        verify(purchaseDataAccess, times(1))
                .savePurchaseDetail(any(PurchaseBean.class));
    }

    @Test
    void testSavePurchaseDetailException() throws Exception {

        PurchaseBean bean = createPurchaseBean();

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenThrow(new PurchaseException());

        assertThrows(PurchaseException.class,
                () -> purchaseService.savePurchaseDetail(bean));

        verify(purchaseDataAccess, times(1))
                .savePurchaseDetail(any(PurchaseBean.class));
    }

    @Test
    void testSavePurchaseDetailReturnsNull() throws Exception {

        PurchaseBean bean = createPurchaseBean();

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(null);

        PurchaseBean response = purchaseService.savePurchaseDetail(bean);

        assertEquals(null, response);
    }

    @Test
    void testQuantityValue() throws Exception {

        PurchaseBean bean = createPurchaseBean();
        bean.setQuantity(100);

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(bean);

        PurchaseBean result = purchaseService.savePurchaseDetail(bean);

        assertEquals(100, result.getQuantity());
    }

    @Test
    void testPurchaseAmount() throws Exception {

        PurchaseBean bean = createPurchaseBean();
        bean.setPurchaseAmount(9999.50);

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(bean);

        PurchaseBean result = purchaseService.savePurchaseDetail(bean);

        assertEquals(9999.50,
                result.getPurchaseAmount());
    }

}