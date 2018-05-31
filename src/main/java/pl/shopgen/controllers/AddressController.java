package pl.shopgen.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Address;
import pl.shopgen.models.User;
import pl.shopgen.repositories.AddressRepository;
import pl.shopgen.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/address")
public class AddressController extends AbstractController {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressController(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAddresses() {
        User user = getLoginUser();
        return mapToJson(user.getAddresses());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addAddress(@RequestBody Address address) {
        User user = getLoginUser();
        Address newAddress=addressRepository.insert(address);
        List<Address> addresses = user.getAddresses() != null ? user.getAddresses() : new ArrayList<>();
        addresses.add(newAddress);
        user.setAddresses(addresses);
        userRepository.save(user);

        return mapToJson(newAddress);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateAddress(@RequestBody Address address) {
        User user=getLoginUser();

        for( Address addressElement: user.getAddresses())
        {
            if(addressElement.getId().equals(address.getId())) {
               addressElement.cloneProperties(address);
            }

        }
        userRepository.save(user);

        return mapToJson(addressRepository.save(address));
    }

    @RequestMapping(value = "/{addressId}" , method = RequestMethod.DELETE)
    public String deleteAddress(@PathVariable("addressId") String addressId) {
        String jsonAddress = mapToJson(addressRepository.findById(addressId).orElse(null));
        User user=getLoginUser();

        if(!jsonAddress.isEmpty()) {
            user.getAddresses().removeIf(address -> address.getId().equals(addressId));
            userRepository.save(user);
            addressRepository.deleteById(addressId);
        }

        return jsonAddress;
    }

    public User getLoginUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }
}
