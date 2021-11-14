package org.spring.mvc.addressbook.models

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

data class Address(val fullName: String?, val address: String?) {
}

@Component
class Addresses(@Autowired private val initialData: InitialData)
{
    fun getListAddresses() = initialData.addresses

    fun addAddress(a: Address) = initialData.addresses.add(a)

    fun removeAddress(id: Int) = initialData.addresses.removeAt(id)

}