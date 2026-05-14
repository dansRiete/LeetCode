package dev.alexkzk.jpa.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@NamedEntityGraph(
    name = "Customer.withOrders",
    attributeNodes = @NamedAttributeNode(value = "orders", subgraph = "orders.items"),
    subgraphs = @NamedSubgraph(
        name = "orders.items",
        attributeNodes = @NamedAttributeNode(value = "items", subgraph = "items.product")
    )
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Set (not List) avoids MultipleBagFetchException when joining multiple @OneToMany at once
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    public Customer() {}

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public Long getId()           { return id; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public Set<Order> getOrders() { return orders; }
}
