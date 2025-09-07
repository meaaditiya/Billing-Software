package in.aaditiya.billingsoftware.service;

import in.aaditiya.billingsoftware.io.CategoryRequest;
import in.aaditiya.billingsoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request, MultipartFile file);
    List<CategoryResponse>  read();
    void delete(String categoryID);
}
