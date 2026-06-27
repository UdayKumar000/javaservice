package com.training.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.training.business.bean.PurchaseBean;
import com.training.dao.PurchaseDataAccess;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private PurchaseDataAccess purchaseDataAccess;

    private PurchaseBean purchaseBean;

    @BeforeEach
    void setUp() {

        purchaseBean = new PurchaseBean();

        purchaseBean.setVendorName("ABC Vendor");
        purchaseBean.setMaterialCategoryId("CAT001");
        purchaseBean.setMaterialTypeId("TYPE001");
        purchaseBean.setBrandName("ACC Cement");
        purchaseBean.setUnitId("UNIT001");
        purchaseBean.setQuantity(10);
        purchaseBean.setPurchaseAmount(1500.0);
        purchaseBean.setPurchaseDate(new Date());
    }

    @Test
    void testAddPurchaseDetails_Success() throws Exception {

        PurchaseBean savedBean = new PurchaseBean();

        savedBean.setPurchaseId(1);
        savedBean.setVendorName("ABC Vendor");
        savedBean.setTransactionId("P_ABC_01012025_CAT");

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenReturn(savedBean);

        PurchaseBean result = purchaseService.addPurchaseDetails(purchaseBean);

        assertNotNull(result);
        assertEquals(1, result.getPurchaseId());
        assertEquals("ABC Vendor", result.getVendorName());

        verify(purchaseDataAccess, times(1))
                .savePurchaseDetail(any(PurchaseBean.class));
    }

    @Test
    void testAddPurchaseDetails_DAOException() throws Exception {

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenThrow(new RuntimeException("Database Error"));

        assertThrows(RuntimeException.class, () -> {
            purchaseService.addPurchaseDetails(purchaseBean);
        });

        verify(purchaseDataAccess, times(1))
                .savePurchaseDetail(any(PurchaseBean.class));
    }

    @Test
    void testTransactionIdGenerated() throws Exception {

        PurchaseBean savedBean = new PurchaseBean();

        when(purchaseDataAccess.savePurchaseDetail(any(PurchaseBean.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PurchaseBean result = purchaseService.addPurchaseDetails(purchaseBean);

        assertNotNull(result.getTransactionId());
        assertEquals(true, result.getTransactionId().startsWith("P_"));
    }

    @Test
    void testSafePrefix_WithNullUsingReflection() throws Exception {

        java.lang.reflect.Method method = PurchaseServiceImpl.class.getDeclaredMethod(
                "safePrefix",
                String.class);

        method.setAccessible(true);

        String value = (String) method.invoke(purchaseService, (String) null);

        assertEquals("NA", value);
    }

    @Test
    void testSafePrefix_WithThreeCharacters() throws Exception {

        java.lang.reflect.Method method = PurchaseServiceImpl.class.getDeclaredMethod(
                "safePrefix",
                String.class);

        method.setAccessible(true);

        String value = (String) method.invoke(purchaseService, "cement");

        assertEquals("CEM", value);
    }

    @Test
    void testSafePrefix_WithLessThanThreeCharacters() throws Exception {

        java.lang.reflect.Method method = PurchaseServiceImpl.class.getDeclaredMethod(
                "safePrefix",
                String.class);

        method.setAccessible(true);

        String value = (String) method.invoke(purchaseService, "AB");

        assertEquals("AB", value);
    }

    @Test
    void testTransactionIdGenerator() throws Exception {

        java.lang.reflect.Method method = PurchaseServiceImpl.class.getDeclaredMethod(
                "transactionIdGenerator",
                String.class,
                String.class,
                Date.class);

        method.setAccessible(true);

        String transactionId = (String) method.invoke(
                purchaseService,
                "Vendor",
                "Category",
                new Date());

        assertNotNull(transactionId);
        assertEquals(true, transactionId.startsWith("P_"));
    }
}