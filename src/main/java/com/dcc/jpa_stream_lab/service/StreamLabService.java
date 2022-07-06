package com.dcc.jpa_stream_lab.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.jpa_stream_lab.repository.ProductsRepository;
import com.dcc.jpa_stream_lab.repository.RolesRepository;
import com.dcc.jpa_stream_lab.repository.ShoppingcartItemRepository;
import com.dcc.jpa_stream_lab.repository.UsersRepository;
import com.dcc.jpa_stream_lab.models.Product;
import com.dcc.jpa_stream_lab.models.Role;
import com.dcc.jpa_stream_lab.models.ShoppingcartItem;
import com.dcc.jpa_stream_lab.models.User;

@Transactional
@Service
public class StreamLabService {
	
	@Autowired
	private ProductsRepository products;
	@Autowired
	private RolesRepository roles;
	@Autowired
	private UsersRepository users;
	@Autowired
	private ShoppingcartItemRepository shoppingcartitems;

    public long ProblemOne() {
    	// Write a query that returns the number of users in the Users table.
        //HINT: Use .count()
    	return users.findAll().stream().count();
    }
    
    public List<User> ProblemTwo()
    {
        // Write a query that retrieves all the users from the User tables.
    	return users.findAll();

    }

    public List<Product> ProblemThree()
    {
        // Write a query that gets each product where the products price is greater than $150.
    	return products.findAll().stream().filter(p -> p.getPrice() > 150).toList();
    }

    public List<Product> ProblemFour()
    {
        // Write a query that gets each product that CONTAINS an "s" in the products name.
        // Return the list
    	return products.findAll().stream().filter(p -> p.getName().contains("s")).toList();
    }

    public List<User> ProblemFive() throws ParseException {
        // Write a query that gets all of the users who registered BEFORE 2016
        // Return the list
        // Research 'java compare dates'
        // You may need to use the helper classes imported above!
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("2016-01-01");
    	return users.findAll().stream().filter(d -> d.getRegistrationDate().before(d1)).toList();
    }

    public List<User> ProblemSix() throws ParseException {
        // Write a query that gets all of the users who registered AFTER 2016 and BEFORE 2018
        // Return the list
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("2016-01-01");
        Date d2 = sdf.parse("2018-01-01");
        return users.findAll().stream().filter(d -> d.getRegistrationDate().before(d2) && d.getRegistrationDate().after(d1)).toList();
    }

    // <><><><><><><><> R Actions (Read) with Foreign Keys <><><><><><><><><>

    public List<User> ProblemSeven()
    {
        // Write a query that retrieves all of the users who are assigned to the role of Customer.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();

    	return customers;
    }

    public List<Product> ProblemEight()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "afton@gmail.com".
        // Return the list
        User customer = users.findAll().stream().filter(u -> u.getEmail().equals("afton@gmail.com")).findFirst().orElse(null);
        assert customer != null;
        List<ShoppingcartItem> cart = customer.getShoppingcartItems();
        List<Product> items = cart.stream().map(ShoppingcartItem::getProduct).toList();
        return items;
    }

    public long ProblemNine()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "oda@gmail.com" and returns the sum of all of the products prices.
    	// Remember to break the problem down and take it one step at a time!
        User customer = users.findAll().stream().filter(u -> u.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        assert customer != null;
        List<ShoppingcartItem> cart = customer.getShoppingcartItems();
        List<Product> items = cart.stream().map(ShoppingcartItem::getProduct).toList();
        List<Integer> prices = items.stream().map(Product::getPrice).toList();
        int sum = prices.stream().mapToInt(Integer::intValue).sum();
    	return sum;
    }

    public List<Product> ProblemTen()
    {
        // Write a query that retrieves all of the products in the shopping cart of users who have the role of "Employee".
    	// Return the list
        Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Employee")).findFirst().orElse(null);
        List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();
        List<ShoppingcartItem> cart = customers.stream().flatMap(i -> i.getShoppingcartItems().stream()).toList();
        List<Product> items = cart.stream().map(ShoppingcartItem::getProduct).toList();
        return items;
    }

    // <><><><><><><><> CUD (Create, Update, Delete) Actions <><><><><><><><><>

    // <><> C Actions (Create) <><>

    public User ProblemEleven()
    {
        // Create a new User object and add that user to the Users table.
        User newUser = new User();        
        newUser.setEmail("david@gmail.com");
        newUser.setPassword("DavidsPass123");
        users.save(newUser);
        return newUser;
    }

    public Product ProblemTwelve()
    {
        // Create a new Product object and add that product to the Products table.
        // Return the product
    	Product newProduct = new Product();
        newProduct.setDescription("Bleach");
        newProduct.setName("Clorox");
        newProduct.setPrice(20);
        products.save(newProduct);
    	return newProduct;

    }

    public List<Role> ProblemThirteen()
    {
        // Add the role of "Customer" to the user we just created in the UserRoles junction table.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
    	david.addRole(customerRole);
    	return david.getRoles();
    }

    public ShoppingcartItem ProblemFourteen()
    {
    	// Create a new ShoppingCartItem to represent the new product you created being added to the new User you created's shopping cart.
        // Add the product you created to the user we created in the ShoppingCart junction table.
        // Return the ShoppingcartItem
        User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
        Product bleach = products.findAll().stream().filter(c -> c.getName().equals("Clorox")).findFirst().orElse(null);
        ShoppingcartItem newItem = new ShoppingcartItem();
        newItem.setProduct(bleach);
        newItem.setUser(david);
        newItem.setQuantity(1);
        shoppingcartitems.save(newItem);
    	return newItem;
    	
    }

    // <><> U Actions (Update) <><>

    public User ProblemFifteen()
    {
         //Update the email of the user we created in problem 11 to "mike@gmail.com"
          User user = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
          user.setEmail("mike@gmail.com");
          return user;
    }

    public Product ProblemSixteen()
    {
        // Update the price of the product you created to a different value.
        // Return the updated product
    	return null;
    }

    public User ProblemSeventeen()
    {
        // Change the role of the user we created to "Employee"
        // HINT: You need to delete the existing role relationship and then create a new UserRole object and add it to the UserRoles table

    	return null;
    }

    
    
	

}
