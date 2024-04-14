package com.embeddedsystems.smartparkingbackend.exceptions.types

class EntityNotFoundException(entityType: String, id: Long): RuntimeException("$entityType with id=$id was not found")
class EntityNotFoundByNameException(entityType: String, name: String): RuntimeException("$entityType with id=$name was not found")
