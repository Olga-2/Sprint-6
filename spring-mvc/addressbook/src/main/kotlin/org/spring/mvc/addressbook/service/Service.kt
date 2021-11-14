package org.spring.mvc.addressbook.service

import org.spring.mvc.addressbook.models.Address
import org.spring.mvc.addressbook.models.Addresses
import org.spring.mvc.addressbook.models.User
import org.spring.mvc.addressbook.models.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Service {

    @Autowired
    private lateinit var users: Users

    @Autowired
    private lateinit var addresses: Addresses

    fun getLogin (user: User) = users.geLoginUsers().find { it.login.equals(user.login, true) && it.password.equals(user.password) }

    fun getList () = addresses.getListAddresses()

    fun getAddressByFio(fio: String) = addresses.getListAddresses().find { it.fullName.equals(fio, true) }

    fun getAddressById(id: Int) = addresses.getListAddresses().get(id)

    fun addAddress(address: Address) = addresses.addAddress(address)

    fun deleteAddress(id: Int) = addresses.removeAddress(id)

    fun editAddress(id: Int, address: Address) = addresses.getListAddresses().set(id, address)


}