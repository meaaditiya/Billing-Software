package in.aaditiya.billingsoftware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.aaditiya.billingsoftware.io.CategoryRequest;
import in.aaditiya.billingsoftware.io.ItemRequest;
import in.aaditiya.billingsoftware.io.ItemResponse;
import in.aaditiya.billingsoftware.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/items")
    public ItemResponse addItem(@RequestPart("item")String itemString,
                                @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest itemRequest = null;
        try {
            itemRequest =  objectMapper.readValue(itemString,ItemRequest.class);
            return itemService.add(itemRequest, file);
        }catch(JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error Ocurred while processing the json");
        }
    }
    @GetMapping("/items")
    public List<ItemResponse> readItems(){
        return itemService.fetchItems();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/items/{itemId}")
    public void deleteItem(@PathVariable String itemId){
          try{
              itemService.deleteItem(itemId);
          }catch(Exception ex){
              throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Item not found");
          }

    }

}
