@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "report/controller/getPurchaseDetails",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<PurchaseBean>> getPurchaseDetails(
        @RequestBody VendorWisePurchaseReportBean bean)
        throws MicroServiceException {

    LOGGER.info("Execution Started [getPurchaseDetails]");

    List<PurchaseBean> reportList = reportsService.getVendorWisePurchaseDetails(
            bean.getFromDate(),
            bean.getToDate(),
            bean.getVendorName());

    // Load master data only once
    List<MaterialCategoryBean> categories =
            materialCategoryConsumer.getMaterialCategoryBeanList();

    List<MaterialTypeBean> materialTypes =
            materialTypeConsumer.getMaterialTypeBeanList();

    List<UnitBean> units =
            unitServiceConsumer.getUnitBeanList();

    if (reportList != null) {

        for (PurchaseBean purchase : reportList) {

            // Category Name
            if (categories != null) {
                for (MaterialCategoryBean category : categories) {
                    if (category.getCategoryId().equals(
                            purchase.getMaterialCategoryId())) {

                        purchase.setMaterialCategoryName(
                                category.getCategoryName());
                        break;
                    }
                }
            }

            // Material Type Name
            if (materialTypes != null) {
                for (MaterialTypeBean type : materialTypes) {
                    if (type.getTypeId().equals(
                            purchase.getMaterialTypeId())) {

                        purchase.setMaterialTypeName(
                                type.getTypeName());
                        break;
                    }
                }
            }

            // Unit Name
            if (units != null) {
                for (UnitBean unit : units) {
                    if (unit.getUnitId().equals(
                            purchase.getUnitId())) {

                        purchase.setMaterialUnitName(
                                unit.getUnitName());
                        break;
                    }
                }
            }
        }
    }

    LOGGER.info("report list {}", reportList);
    LOGGER.info("Execution Over [getPurchaseDetails]");

    return ResponseEntity.ok(reportList);
}