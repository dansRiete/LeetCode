package dev.alexkzk.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@NamedEntityGraph(
    name = "Order.withItems",
    attributeNodes = @NamedAttributeNode(value = "items", subgraph = "items.product"),
    subgraphs = @NamedSubgraph(
        name = "items.product",
        attributeNodes = @NamedAttributeNode("product")
    )
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Set (not List) avoids MultipleBagFetchException when joining multiple @OneToMany at once
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    public Order() {}

    public Order(OrderStatus status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public Long getId()                { return id; }
    public OrderStatus getStatus()     { return status; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public Customer getCustomer()      { return customer; }
    public Set<OrderItem> getItems()   { return items; }

    public enum OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED }
}
