package com.training.dao;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.training.business.bean.PurchaseBean;
import com.training.entity.PurchaseEntity;

@ExtendWith(MockitoExtension.class)
public class PurchaseDataAccessTest {

    @InjectMocks
    private PurchaseDataAccess purchaseDataAccess;

    @Mock
    private PurchaseDAO purchaseDAO;

    private PurchaseBean purchaseBean;
    private PurchaseEntity purchaseEntity;

    @BeforeEach
    void setUp() {

        purchaseBean = new PurchaseBean();
        purchaseBean.setVendorName("ABC Vendor");
        purchaseBean.setMaterialCategoryId("CAT001");
        purchaseBean.setMaterialTypeId("TYPE001");
        purchaseBean.setBrandName("ACC Cement");
        purchaseBean.setUnitId("UNIT001");
        purchaseBean.setQuantity(10);
        purchaseBean.setPurchaseAmount(2500.0);
        purchaseBean.setPurchaseDate(new Date());
        purchaseBean.setTransactionId("P_ABC_01012025_CAT");

        purchaseEntity = new PurchaseEntity();
        purchaseEntity.setPurchaseId(1);
        purchaseEntity.setVendorName("ABC Vendor");
        purchaseEntity.setMaterialCategoryId("CAT001");
        purchaseEntity.setMaterialTypeId("TYPE001");
        purchaseEntity.setBrandName("ACC Cement");
        purchaseEntity.setUnitId("UNIT001");
        purchaseEntity.setQuantity(10);
        purchaseEntity.setPurchaseAmount(2500.0);
        purchaseEntity.setPurchaseDate(purchaseBean.getPurchaseDate());
        purchaseEntity.setTransactionId("P_ABC_01012025_CAT");
        purchaseEntity.setStatus("SUCCESS");
    }

    @Test
    void testSavePurchaseDetail_Success() throws Exception {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenReturn(purchaseEntity);

        PurchaseBean result = purchaseDataAccess.savePurchaseDetail(purchaseBean);

        assertNotNull(result);
        assertEquals(1, result.getPurchaseId());
        assertEquals("ABC Vendor", result.getVendorName());
        assertEquals("CAT001", result.getMaterialCategoryId());
        assertEquals("TYPE001", result.getMaterialTypeId());
        assertEquals("ACC Cement", result.getBrandName());
        assertEquals("UNIT001", result.getUnitId());
        assertEquals(10, result.getQuantity());
        assertEquals(2500.0, result.getPurchaseAmount());
        assertEquals("SUCCESS", result.getStatus());

        verify(purchaseDAO, times(1))
                .save(any(PurchaseEntity.class));
    }

    @Test
    void testSavePurchaseDetail_VerifyTransactionId() throws Exception {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenReturn(purchaseEntity);

        PurchaseBean result = purchaseDataAccess.savePurchaseDetail(purchaseBean);

        assertEquals(
                "P_ABC_01012025_CAT",
                result.getTransactionId());

        verify(purchaseDAO).save(any(PurchaseEntity.class));
    }

    @Test
    void testSavePurchaseDetail_VerifyStatus() throws Exception {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenReturn(purchaseEntity);

        PurchaseBean result = purchaseDataAccess.savePurchaseDetail(purchaseBean);

        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testSavePurchaseDetail_DatabaseException() {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenThrow(new RuntimeException("Database Error"));

        assertThrows(RuntimeException.class, () -> {

            purchaseDataAccess.savePurchaseDetail(purchaseBean);

        });

        verify(purchaseDAO).save(any(PurchaseEntity.class));
    }

    @Test
    void testSavePurchaseDetail_VerifySaveCalledOnce() throws Exception {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenReturn(purchaseEntity);

        purchaseDataAccess.savePurchaseDetail(purchaseBean);

        verify(purchaseDAO, times(1))
                .save(any(PurchaseEntity.class));
    }

    @Test
    void testSavePurchaseDetail_AllFieldsMapped() throws Exception {

        when(purchaseDAO.save(any(PurchaseEntity.class)))
                .thenReturn(purchaseEntity);

        PurchaseBean bean = purchaseDataAccess.savePurchaseDetail(purchaseBean);

        assertNotNull(bean);
        assertEquals(purchaseEntity.getVendorName(), bean.getVendorName());
        assertEquals(purchaseEntity.getBrandName(), bean.getBrandName());
        assertEquals(purchaseEntity.getMaterialCategoryId(), bean.getMaterialCategoryId());
        assertEquals(purchaseEntity.getMaterialTypeId(), bean.getMaterialTypeId());
        assertEquals(purchaseEntity.getUnitId(), bean.getUnitId());
        assertEquals(purchaseEntity.getPurchaseAmount(), bean.getPurchaseAmount());
        assertEquals(purchaseEntity.getQuantity(), bean.getQuantity());
        assertEquals(purchaseEntity.getPurchaseDate(), bean.getPurchaseDate());
    }
}