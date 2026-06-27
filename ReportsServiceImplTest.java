package com.training.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.training.business.bean.PurchaseBean;
import com.training.dao.ReportsDataAccess;

@ExtendWith(MockitoExtension.class)
public class ReportsServiceImplTest {

    @InjectMocks
    private ReportsServiceImpl reportsService;

    @Mock
    private ReportsDataAccess reportsDataAccess;

    private PurchaseBean purchaseBean;
    private List<PurchaseBean> purchaseList;

    @BeforeEach
    void setUp() {

        purchaseBean = new PurchaseBean();
        purchaseBean.setPurchaseId(1);
        purchaseBean.setVendorName("ABC Vendor");
        purchaseBean.setMaterialCategoryId("CAT001");
        purchaseBean.setMaterialTypeId("TYPE001");
        purchaseBean.setBrandName("ACC Cement");
        purchaseBean.setUnitId("UNIT001");
        purchaseBean.setQuantity(15);
        purchaseBean.setPurchaseAmount(2500.00);
        purchaseBean.setPurchaseDate(new Date());

        purchaseList = new ArrayList<>();
        purchaseList.add(purchaseBean);
    }

    @Test
    void testGetVendorWisePurchaseDetails_Success() {

        when(reportsDataAccess.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(purchaseList);

        List<PurchaseBean> result = reportsService.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC Vendor", result.get(0).getVendorName());

        verify(reportsDataAccess, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testGetVendorWisePurchaseDetails_EmptyList() {

        when(reportsDataAccess.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(new ArrayList<>());

        List<PurchaseBean> result = reportsService.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(reportsDataAccess, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testGetVendorWisePurchaseDetails_NullList() {

        when(reportsDataAccess.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(null);

        List<PurchaseBean> result = reportsService.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        assertEquals(null, result);

        verify(reportsDataAccess, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testGetVendorWisePurchaseDetails_Exception() {

        when(reportsDataAccess.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenThrow(new RuntimeException("Database Error"));

        assertThrows(RuntimeException.class, () -> {

            reportsService.getVendorWisePurchaseDetails(
                    new Date(),
                    new Date(),
                    "ABC Vendor");
        });

        verify(reportsDataAccess, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testVerifyReturnedPurchaseBean() {

        when(reportsDataAccess.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(purchaseList);

        List<PurchaseBean> result = reportsService.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        PurchaseBean bean = result.get(0);

        assertEquals(1, bean.getPurchaseId());
        assertEquals("ABC Vendor", bean.getVendorName());
        assertEquals("CAT001", bean.getMaterialCategoryId());
        assertEquals("TYPE001", bean.getMaterialTypeId());
        assertEquals("ACC Cement", bean.getBrandName());
        assertEquals("UNIT001", bean.getUnitId());
        assertEquals(15, bean.getQuantity());
        assertEquals(2500.00, bean.getPurchaseAmount());
    }
}