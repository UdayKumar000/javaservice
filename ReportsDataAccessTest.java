package com.training.dao;

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
import com.training.entity.PurchaseEntity;

@ExtendWith(MockitoExtension.class)
public class ReportsDataAccessTest {

    @InjectMocks
    private ReportsDataAccess reportsDataAccess;

    @Mock
    private ReportsDAO reportsDAO;

    private PurchaseEntity purchaseEntity;

    @BeforeEach
    void setUp() {

        purchaseEntity = new PurchaseEntity();

        purchaseEntity.setPurchaseId(1);
        purchaseEntity.setTransactionId("P_ABC_01012025_CAT");
        purchaseEntity.setVendorName("ABC Vendor");
        purchaseEntity.setMaterialCategoryId("CAT001");
        purchaseEntity.setMaterialTypeId("TYPE001");
        purchaseEntity.setBrandName("ACC Cement");
        purchaseEntity.setUnitId("UNIT001");
        purchaseEntity.setQuantity(10);
        purchaseEntity.setPurchaseAmount(2500.00);
        purchaseEntity.setPurchaseDate(new Date());
        purchaseEntity.setStatus("SUCCESS");
    }

    @Test
    void testGetVendorWisePurchaseDetails_Success() {

        List<PurchaseEntity> entityList = new ArrayList<>();
        entityList.add(purchaseEntity);

        when(reportsDAO.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(entityList);

        List<PurchaseBean> result = reportsDataAccess.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        assertNotNull(result);
        assertEquals(1, result.size());

        PurchaseBean bean = result.get(0);

        assertEquals(1, bean.getPurchaseId());
        assertEquals("ABC Vendor", bean.getVendorName());
        assertEquals("CAT001", bean.getMaterialCategoryId());
        assertEquals("TYPE001", bean.getMaterialTypeId());
        assertEquals("ACC Cement", bean.getBrandName());
        assertEquals("UNIT001", bean.getUnitId());
        assertEquals(10, bean.getQuantity());
        assertEquals(2500.00, bean.getPurchaseAmount());
        assertEquals("SUCCESS", bean.getStatus());

        verify(reportsDAO, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testGetVendorWisePurchaseDetails_EmptyList() {

        when(reportsDAO.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(new ArrayList<>());

        List<PurchaseBean> result = reportsDataAccess.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(reportsDAO)
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testGetVendorWisePurchaseDetails_Exception() {

        when(reportsDAO.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenThrow(new RuntimeException("Database Error"));

        assertThrows(RuntimeException.class, () -> {

            reportsDataAccess.getVendorWisePurchaseDetails(
                    new Date(),
                    new Date(),
                    "ABC Vendor");

        });

        verify(reportsDAO)
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }

    @Test
    void testVerifyEntityToBeanMapping() {

        List<PurchaseEntity> entityList = List.of(purchaseEntity);

        when(reportsDAO.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(entityList);

        PurchaseBean bean = reportsDataAccess
                .getVendorWisePurchaseDetails(
                        new Date(),
                        new Date(),
                        "ABC Vendor")
                .get(0);

        assertEquals(purchaseEntity.getPurchaseId(), bean.getPurchaseId());
        assertEquals(purchaseEntity.getTransactionId(), bean.getTransactionId());
        assertEquals(purchaseEntity.getVendorName(), bean.getVendorName());
        assertEquals(purchaseEntity.getPurchaseAmount(), bean.getPurchaseAmount());
        assertEquals(purchaseEntity.getPurchaseDate(), bean.getPurchaseDate());
    }

    @Test
    void testRepositoryCalledOnce() {

        when(reportsDAO.getVendorWisePurchaseDetails(
                any(Date.class),
                any(Date.class),
                anyString()))
                .thenReturn(new ArrayList<>());

        reportsDataAccess.getVendorWisePurchaseDetails(
                new Date(),
                new Date(),
                "ABC Vendor");

        verify(reportsDAO, times(1))
                .getVendorWisePurchaseDetails(
                        any(Date.class),
                        any(Date.class),
                        anyString());
    }
}