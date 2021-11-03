package org.spring.mvc.adressbook.models

import org.springframework.stereotype.Component

data class Address(val fullName: String?, val address: String?) {
}

@Component
class Addresses
{
    val addresses = mutableListOf(
        Address("Ivanov Ivan", "Matrosova,  45"),
        Address("Petrov Ivan", "Nikitina,  14"),
        Address("Sidorov Vladimir", "Orechovajа,  26"),
        Address("Ohlabyistin Ivan", "Petrovaka, 38")
    )

    fun getListAddresses() = addresses

    fun addAddress(a: Address) = addresses.add(a)

    fun removeAddress(id: Int) = addresses.removeAt(id)

}