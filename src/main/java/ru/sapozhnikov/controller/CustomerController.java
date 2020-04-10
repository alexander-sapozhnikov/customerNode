package ru.sapozhnikov.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sapozhnikov.dao.AddressDAO;
import ru.sapozhnikov.dao.CustomerDAO;
import ru.sapozhnikov.entity.Address;
import ru.sapozhnikov.entity.Customer;

@Controller
public class CustomerController {
    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;

    @Autowired
    public CustomerController(CustomerDAO customerDAO, AddressDAO addressDAO) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
    }

    @GetMapping("showAllCustomers")
    public String showAllCustomers(Model model){
        model.addAttribute("result", customerDAO.findAll());
        return "showResult";
    }


    @PostMapping("addCustomer")
    public String addCustomer (@NonNull String fristname, @NonNull String lastname,
                      @NonNull String email, @NonNull String password,
                      @NonNull String phone, @NonNull String city,
                      @NonNull String state, @NonNull String country,
                      Model model){

        Address address = new Address();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        addressDAO.save(address);

        Customer customer = new Customer();
        customer.setFirstName(fristname);
        customer.setLastName(lastname);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setAddress(address);

        customerDAO.save(customer);
        return "main";
    }

}
