package com.casadosreclamos.exception

class ObsoleteObjectException(entity: String) : CDRException("O $entity está obsoleto")