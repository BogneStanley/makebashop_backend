package cm.bognestanley.shop_backend.application.order.usecase;

import org.springframework.stereotype.Service;

import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.order.entity.Order;
import cm.bognestanley.shop_backend.domain.order.repository.OrderRepository;
import cm.bognestanley.shop_backend.domain.product.entity.Product;
import cm.bognestanley.shop_backend.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class MarkOrderAsPaidUsecase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public MarkOrderAsPaidUsecase(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order execute(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DomainErrorException(null));

        order.markAsPaid();

        for (Product item : order.getProducts()) {
            productRepository.save(item);
        }

        return orderRepository.save(order);
    }

}
