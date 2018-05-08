package pl.shopgen.services;

import org.springframework.data.domain.Page;
import pl.shopgen.models.Product;

public interface ProductService {
    Page<Product> findPaginated(int page, int size);
}
