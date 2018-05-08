package pl.shopgen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.exceptions.PageOutOfBoundException;
import pl.shopgen.models.Product;
import pl.shopgen.services.ProductService;

@RestController
public class ProductPagination {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/products",params = {"page","size"},method = RequestMethod.GET)
    public Page<Product> findProductPaginated(@RequestParam("page") int page,@RequestParam("size") int size)
    {
        Page<Product> resultPage = productService.findPaginated(page, size);
        if(page > resultPage.getTotalPages())
        {
            throw new PageOutOfBoundException();
        }
        return resultPage;
    }
}
