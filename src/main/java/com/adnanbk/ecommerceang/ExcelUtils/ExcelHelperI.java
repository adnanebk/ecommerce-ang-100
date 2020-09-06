package com.adnanbk.ecommerceang.ExcelUtils;

import com.adnanbk.ecommerceang.models.Product;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelHelperI<T> {

    boolean hasExcelFormat(MultipartFile file);

    List<T> excelToList(InputStream is);
    ByteArrayInputStream listToExcel(List<T> list);
    List<T> getList();

}
