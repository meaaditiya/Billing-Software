package in.aaditiya.billingsoftware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.aaditiya.billingsoftware.io.CategoryRequest;
import in.aaditiya.billingsoftware.io.CategoryResponse;
import in.aaditiya.billingsoftware.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController

@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService service;
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestPart("category") String categoryString,
                                        @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CategoryRequest request=  objectMapper.readValue(categoryString,CategoryRequest.class);
            return service.add(request, file);
        } catch (JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occurred while parsing the json");
        }

    }
    @GetMapping("/categories")
    public List<CategoryResponse> fetchCategories(){
        return  service.read();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/categories/{categoryId}")
    public void remove(@PathVariable String categoryId){
           try{
               service.delete(categoryId);
           }catch (Exception e){
               throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found");
           }
    }
}
