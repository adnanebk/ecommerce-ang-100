package com.adnanbk.ecommerceang.ExcelUtils;

import com.adnanbk.ecommerceang.models.Product;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Component
public class ExcelHelperProduct implements ExcelHelperI<Product> {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs ={ "Id", "Name", "Description", "Sku","Price","Quantity",
                               "Category name","Active","Image url","Created date","Updated date"};

    static String SHEET = "Products";
    private List<Product> products=new ArrayList<>();

    @Override
    public  boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    @Override
    public  List<Product> excelToList(InputStream is) {

        List<Product> products = new ArrayList<>();
        try ( Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                currentRow.getFirstCellNum();
                if(currentRow.getPhysicalNumberOfCells()<=0)
                    continue;
                Product product =new Product();
                for(int i=0; i<currentRow.getLastCellNum(); i++) {
                    var currentCell = currentRow.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if(currentCell==null)
                        continue;
                    try {
                    switch (i) {
                        case 0:
                            if(currentCell.getCellType().equals(CellType.NUMERIC))
                                product.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            product.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            product.setDescription(currentCell.getStringCellValue());
                            break;
                        case 3:
                            product.setSku(currentCell.getStringCellValue());
                            break;
                        case 4:
                            product.setUnitPrice(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                            break;
                        case 5:
                            product.setUnitsInStock((int) currentCell.getNumericCellValue());
                            break;
                        case 6:
                            product.setCategoryName(currentCell.getStringCellValue());
                            break;
                        case 7:
                            product.setActive(currentCell.getBooleanCellValue());
                            break;
                        case 8:
                            product.setImageUrl(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                } catch (IllegalStateException ex){
                        throw new ValidationException("fail to load data from Excel file: , check if you are using valid data with correct orders");

                    }

                }



                products.add(product);
            }

           // workbook.close();
            this.products.addAll(products);
            return products;
        } catch (IOException e) {
            throw new ValidationException("fail to parse Excel file: " + e.getMessage());
        }
    }


    public  ByteArrayInputStream listToExcel(List<Product> products) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getSku());
                row.createCell(4).setCellValue(product.getUnitPrice().doubleValue());
                row.createCell(5).setCellValue(product.getUnitsInStock());
                row.createCell(6).setCellValue(product.getCategoryName());
                row.createCell(7).setCellValue(product.isActive());
                row.createCell(8).setCellValue(product.getImageUrl());
                row.createCell(9).setCellValue(format.format(product.getDateCreated()));
                row.createCell(10).setCellValue(product.getLastUpdated());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new ValidationException("fail to import data to Excel file: ");
        }
    }


    @Override
    public List<Product> getList() {
        return products;
    }

}
