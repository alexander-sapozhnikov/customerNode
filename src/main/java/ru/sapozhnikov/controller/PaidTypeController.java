package ru.sapozhnikov.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sapozhnikov.dao.AddressDAO;
import ru.sapozhnikov.dao.CustomerDAO;
import ru.sapozhnikov.dao.PaidTypeDAO;
import ru.sapozhnikov.entity.Address;
import ru.sapozhnikov.entity.Customer;
import ru.sapozhnikov.entity.PaidType;

@RestController
@RequestMapping("/paidTypes")
public class PaidTypeController {
    @Autowired
    private PaidTypeDAO paidTypeDAO;

    @GetMapping
    public ResponseEntity showAllCustomers(){
        return ResponseEntity.ok(paidTypeDAO.findAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity showByIdPaidType(@RequestParam String id){
        try{
            return ResponseEntity.ok(paidTypeDAO.getById(Integer.valueOf(id)));
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity addPaidType (String name){
        PaidType paidType = new PaidType();
        paidType.setName(name);
        paidTypeDAO.save(paidType);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity deletePaidType(@RequestParam String id){
        try{
            paidTypeDAO.delete(paidTypeDAO.getById(Integer.valueOf(id)));
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping
    public ResponseEntity updatePaidType(@RequestParam String id, @RequestParam String name){
        PaidType paidType = null;
        try{
            paidType = paidTypeDAO.getById(Integer.valueOf(id));
            if(paidType == null) throw new NullPointerException();
        } catch (NumberFormatException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        paidType.setName(name);
        paidTypeDAO.save(paidType);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
