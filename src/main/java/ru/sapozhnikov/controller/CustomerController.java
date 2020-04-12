package ru.sapozhnikov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sapozhnikov.dao.AddressDAO;
import ru.sapozhnikov.dao.CustomerDAO;
import ru.sapozhnikov.dao.PaidTypeDAO;
import ru.sapozhnikov.entity.Address;
import ru.sapozhnikov.entity.Customer;
import ru.sapozhnikov.entity.PaidType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class CustomerController {
    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private PaidTypeDAO paidTypeDAO;

    @Autowired
    public CustomerController(CustomerDAO customerDAO, AddressDAO addressDAO, PaidTypeDAO paidTypeDAO) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
        this.paidTypeDAO = paidTypeDAO;
    }

    @GetMapping
    public String mainWind(){
        return "index";
    }

    @GetMapping(path = "allCustomers")
    public ResponseEntity showAllCustomers(){
        return ResponseEntity.ok(customerDAO.findAll());
    }

    @GetMapping("byIdCustomer")
    public ResponseEntity showByIdCustomer(@RequestParam String id){
        try{
            return ResponseEntity.ok(customerDAO.getById(Integer.valueOf(id)));
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("addCustomer")
    public ResponseEntity addCustomer ( String firstname,  String lastname,
                                        String email,  String password,
                                        String phone,  String city,
                                        String state,  String country,
                                        int paidTypeId){
        Address address = new Address();
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        addressDAO.save(address);

        Customer customer = new Customer();
        customer.setFirstName(firstname);
        customer.setLastName(lastname);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setPaidTypeList(Arrays.asList(paidTypeDAO.getById(paidTypeId)));
        customerDAO.save(customer);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("deleteCustomer")
    public ResponseEntity deleteCustomer(@RequestParam String id){
        try{
            customerDAO.delete(customerDAO.getById(Integer.valueOf(id)));
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateCustomer")
    public ResponseEntity updateCustomer(@RequestParam String id, @RequestParam String field,
                                         @RequestParam String value){
        Customer customer;
        try{
            customer = customerDAO.getById(Integer.valueOf(id));
            if(customer == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Address address = customer.getAddress();
        switch (field){
            case "firstname" : customer.setFirstName(value); break;
            case "lastname" : customer.setLastName(value); break;
            case "email" : customer.setEmail(value); break;
            case "password" : customer.setPassword(value); break;
            case "phone" : customer.setPhone(value); break;
            case "city" : address.setCity(value); break;
            case "state" : address.setState(value); break;
            case "country" : address.setCountry(value); break;
        }
        customer.setAddress(address);

        List<PaidType> paidTypes = customer.getPaidTypeList();
        if("paidType".equals(field)){
            try{
                PaidType paidType = paidTypeDAO.getById(Integer.parseInt(value));
                if(paidTypes.contains(paidType)){
                    paidTypes.remove(paidType);
                } else {
                    paidTypes.add(paidType);
                }
            } catch (NumberFormatException e){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        customer.setPaidTypeList(paidTypes);

        customerDAO.save(customer);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
