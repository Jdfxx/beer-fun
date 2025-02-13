package pl.filiphagno.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDto);
    CustomerDTO customerToCustomerDTO(Customer customer);
}
