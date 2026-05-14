package dev.alexkzk.jpa;

import dev.alexkzk.jpa.entity.Customer;
import dev.alexkzk.jpa.entity.Order;
import dev.alexkzk.jpa.entity.Order.OrderStatus;
import dev.alexkzk.jpa.entity.OrderItem;
import dev.alexkzk.jpa.entity.Product;
import jakarta.persistence.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates @EntityGraph patterns and the N+1 problem.
 *
 * Schema: Customer --< Order --< OrderItem >-- Product
 * All associations are LAZY by default.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityGraphExamplesTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("jpa-practise");
        seedData();
    }

    @AfterAll
    static void destroy() {
        emf.close();
    }

    // -------------------------------------------------------------------------
    // 1. N+1 problem — baseline (no EntityGraph)
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(1)
    void nPlusOneProblem() {
        System.out.println("\n=== N+1: loads customers (1 query), then lazily triggers a query per customer ===");

        EntityManager em = emf.createEntityManager();
        try {
            // 1 query for customers
            List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class)
                                         .getResultList();

            // N queries — one per customer to load orders
            for (Customer c : customers) {
                int orderCount = c.getOrders().size(); // triggers SELECT ... WHERE customer_id = ?
                System.out.println(c.getName() + " has " + orderCount + " orders");
            }
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 2. @EntityGraph via named graph — flat (one level deep)
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(2)
    void namedEntityGraph_customersWithOrders() {
        System.out.println("\n=== Named EntityGraph: Customer.withOrders — single JOIN query ===");

        EntityManager em = emf.createEntityManager();
        try {
            EntityGraph<?> graph = em.getEntityGraph("Customer.withOrders");

            List<Customer> customers = em.createQuery("SELECT DISTINCT c FROM Customer c", Customer.class)
                                         .setHint("jakarta.persistence.fetchgraph", graph)
                                         .getResultList();

            for (Customer c : customers) {
                System.out.printf("%-15s orders: %d%n", c.getName(), c.getOrders().size());
            }

            assertFalse(customers.isEmpty());
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 3. @EntityGraph with attributePaths (inline, no @NamedEntityGraph needed)
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(3)
    void inlineAttributePaths_ordersWithItemsAndProducts() {
        System.out.println("\n=== Inline attributePaths: Order + items + items.product in one query ===");

        EntityManager em = emf.createEntityManager();
        try {
            EntityGraph<Order> graph = em.createEntityGraph(Order.class);
            Subgraph<OrderItem> itemsGraph = graph.addSubgraph("items");
            itemsGraph.addAttributeNodes("product");

            List<Order> orders = em.createQuery("SELECT DISTINCT o FROM Order o", Order.class)
                                    .setHint("jakarta.persistence.fetchgraph", graph)
                                    .getResultList();

            for (Order o : orders) {
                BigDecimal total = o.getItems().stream()
                                    .map(OrderItem::subtotal)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                System.out.printf("Order#%d [%s] items: %d  total: %s%n",
                        o.getId(), o.getStatus(), o.getItems().size(), total);

                o.getItems().forEach(item ->
                    System.out.printf("  - %s x%d @ %s%n",
                            item.getProduct().getName(), item.getQuantity(), item.getUnitPrice()));
            }

            assertFalse(orders.isEmpty());
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 4. FETCH vs LOAD graph type
    //    FETCH: listed attrs → EAGER, all others → LAZY (ignores declared fetch type)
    //    LOAD:  listed attrs → EAGER, all others → use their declared fetch type
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(4)
    void fetchGraphVsLoadGraph() {
        System.out.println("\n=== FETCH graph: only 'items' eager; customer stays LAZY ===");

        EntityManager em = emf.createEntityManager();
        try {
            EntityGraph<Order> graph = em.createEntityGraph(Order.class);
            graph.addAttributeNodes("items");

            // jakarta.persistence.fetchgraph → FETCH semantics
            List<Order> orders = em.createQuery("SELECT o FROM Order o", Order.class)
                                    .setHint("jakarta.persistence.fetchgraph", graph)  // items eager, customer lazy
                                    .getResultList();

            System.out.println("Orders loaded: " + orders.size());

            // Switch to LOAD semantics
            System.out.println("\n=== LOAD graph: 'items' eager; customer uses its own FetchType.LAZY ===");
            em.clear();

            orders = em.createQuery("SELECT o FROM Order o", Order.class)
                        .setHint("jakarta.persistence.loadgraph", graph)   // items eager, customer = declared LAZY
                        .getResultList();

            System.out.println("Orders loaded: " + orders.size());
            assertFalse(orders.isEmpty());
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 5. Named graph on Order entity (Order.withItems defined via @NamedEntityGraph)
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(5)
    void namedEntityGraph_orderWithItemsAndProducts() {
        System.out.println("\n=== Named EntityGraph: Order.withItems (items + nested product) ===");

        EntityManager em = emf.createEntityManager();
        try {
            EntityGraph<?> graph = em.getEntityGraph("Order.withItems");

            List<Order> orders = em.createQuery("SELECT DISTINCT o FROM Order o WHERE o.status = :status", Order.class)
                                    .setParameter("status", OrderStatus.CONFIRMED)
                                    .setHint("jakarta.persistence.fetchgraph", graph)
                                    .getResultList();

            orders.forEach(o -> System.out.printf("Order#%d items: %s%n",
                    o.getId(),
                    o.getItems().stream().map(i -> i.getProduct().getName()).toList()));
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 6. Pagination-safe pattern — two queries to avoid in-memory pagination
    //    (Hibernate warns: HHH90003004 when you JOIN a collection + paginate)
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(6)
    void paginationSafe_twoQueryPattern() {
        System.out.println("\n=== Pagination-safe: fetch IDs first, then load with graph ===");

        EntityManager em = emf.createEntityManager();
        try {
            // Query 1: paginate on scalar IDs — no collection JOIN, no in-memory issue
            List<Long> pagedIds = em.createQuery("SELECT c.id FROM Customer c ORDER BY c.name", Long.class)
                                     .setFirstResult(0)
                                     .setMaxResults(2)
                                     .getResultList();

            System.out.println("Paged customer IDs: " + pagedIds);

            // Query 2: load full graph only for those IDs
            EntityGraph<Customer> graph = em.createEntityGraph(Customer.class);
            graph.addSubgraph("orders").addAttributeNodes("items");

            List<Customer> customers = em.createQuery(
                            "SELECT DISTINCT c FROM Customer c WHERE c.id IN :ids", Customer.class)
                                          .setParameter("ids", pagedIds)
                                          .setHint("jakarta.persistence.fetchgraph", graph)
                                          .getResultList();

            customers.forEach(c -> System.out.printf("%-15s orders: %d%n", c.getName(), c.getOrders().size()));
            assertEquals(pagedIds.size(), customers.size());
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // 7. find() with EntityGraph — single entity lookup
    // -------------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Order(7)
    void findWithEntityGraph() {
        System.out.println("\n=== EntityManager.find() with EntityGraph ===");

        EntityManager em = emf.createEntityManager();
        try {
            Long firstCustomerId = em.createQuery("SELECT c.id FROM Customer c ORDER BY c.id", Long.class)
                                      .setMaxResults(1)
                                      .getSingleResult();

            EntityGraph<?> graph = em.getEntityGraph("Customer.withOrders");

            Customer customer = em.find(Customer.class, firstCustomerId,
                    Map.of("jakarta.persistence.fetchgraph", graph));

            assertNotNull(customer);
            System.out.printf("Customer: %s, orders eagerly loaded: %d%n",
                    customer.getName(), customer.getOrders().size());
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------
    // Seed data
    // -------------------------------------------------------------------------

    private static void seedData() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Product laptop  = new Product("Laptop",   new BigDecimal("1299.99"));
            Product mouse   = new Product("Mouse",    new BigDecimal("29.99"));
            Product monitor = new Product("Monitor",  new BigDecimal("499.99"));
            Product keyboard = new Product("Keyboard", new BigDecimal("79.99"));
            em.persist(laptop);
            em.persist(mouse);
            em.persist(monitor);
            em.persist(keyboard);

            Customer alice = new Customer("Alice",   "alice@example.com");
            Customer bob   = new Customer("Bob",     "bob@example.com");
            Customer carol = new Customer("Carol",   "carol@example.com");

            Order o1 = new Order(OrderStatus.CONFIRMED);
            o1.addItem(new OrderItem(laptop, 1));
            o1.addItem(new OrderItem(mouse,  2));
            alice.addOrder(o1);

            Order o2 = new Order(OrderStatus.SHIPPED);
            o2.addItem(new OrderItem(monitor, 1));
            alice.addOrder(o2);

            Order o3 = new Order(OrderStatus.PENDING);
            o3.addItem(new OrderItem(keyboard, 1));
            o3.addItem(new OrderItem(mouse,    1));
            bob.addOrder(o3);

            Order o4 = new Order(OrderStatus.CONFIRMED);
            o4.addItem(new OrderItem(laptop,   2));
            o4.addItem(new OrderItem(monitor,  1));
            o4.addItem(new OrderItem(keyboard, 1));
            carol.addOrder(o4);

            em.persist(alice);
            em.persist(bob);
            em.persist(carol);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
