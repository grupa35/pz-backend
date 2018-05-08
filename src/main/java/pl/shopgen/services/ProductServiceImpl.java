package pl.shopgen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository dao;

    @Override
    public Page<Product> findPaginated(int page, int size) {
        return dao.findAll(PageRequest.of(page, size));
    }
}
