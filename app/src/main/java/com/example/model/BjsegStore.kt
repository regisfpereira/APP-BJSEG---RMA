package com.example.model

data class BjsegStore(
    val id: String,
    val name: String,
    val isDistributionCenter: Boolean = false,
    val address: String,
    val neighborhood: String,
    val city: String,
    val state: String = "SP",
    val postalCode: String,
    val phone: String,
    val openingHours: String,
    val latitude: Double,
    val longitude: Double
) {
    val fullAddress: String
        get() = "$address, $neighborhood, $city - $state, CEP $postalCode"

    val mapUri: String
        get() = "geo:$latitude,$longitude?q=${java.net.URLEncoder.encode("$name, $address, $city", "UTF-8")}"

    val webMapUrl: String
        get() = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

    companion object {
        val OFFICIAL_STORES = listOf(
            BjsegStore(
                id = "itaqua",
                name = "Itaquaquecetuba — Loja e Centro de Distribuição",
                isDistributionCenter = true,
                address = "Rodovia Alberto Hinoto, 4500",
                neighborhood = "Jardim Odete",
                city = "Itaquaquecetuba",
                postalCode = "08598-000",
                phone = "(11) 4646-7000",
                openingHours = "Segunda a Sexta: 08:00 às 18:00 | Sábado: 08:00 às 12:00",
                latitude = -23.4862,
                longitude = -46.3484
            ),
            BjsegStore(
                id = "santos",
                name = "Santos",
                isDistributionCenter = false,
                address = "Avenida Ana Costa, 290",
                neighborhood = "Vila Mathias",
                city = "Santos",
                postalCode = "11060-000",
                phone = "(13) 3228-5000",
                openingHours = "Segunda a Sexta: 08:30 às 18:00",
                latitude = -23.9535,
                longitude = -46.3312
            ),
            BjsegStore(
                id = "aruja",
                name = "Arujá",
                isDistributionCenter = false,
                address = "Avenida dos Expedicionários, 1120",
                neighborhood = "Centro",
                city = "Arujá",
                postalCode = "07400-000",
                phone = "(11) 4652-3000",
                openingHours = "Segunda a Sexta: 08:00 às 18:00",
                latitude = -23.3967,
                longitude = -46.3217
            ),
            BjsegStore(
                id = "sjc_loja",
                name = "São José dos Campos — Loja",
                isDistributionCenter = false,
                address = "Avenida São João, 1400",
                neighborhood = "Jardim Esplanada",
                city = "São José dos Campos",
                postalCode = "12242-000",
                phone = "(12) 3942-8000",
                openingHours = "Segunda a Sexta: 08:00 às 18:00 | Sábado: 08:00 às 12:00",
                latitude = -23.1977,
                longitude = -45.8942
            ),
            BjsegStore(
                id = "taubate",
                name = "Taubaté",
                isDistributionCenter = false,
                address = "Avenida Charles Schnneider, 900",
                neighborhood = "Parque Senhor do Bonfim",
                city = "Taubaté",
                postalCode = "12040-000",
                phone = "(12) 3634-9000",
                openingHours = "Segunda a Sexta: 08:00 às 18:00",
                latitude = -23.0298,
                longitude = -45.5562
            ),
            BjsegStore(
                id = "sjc_escritorio",
                name = "Escritório de São José dos Campos",
                isDistributionCenter = false,
                address = "Rua Euclides Miragaia, 660, Sala 82",
                neighborhood = "Centro",
                city = "São José dos Campos",
                postalCode = "12245-820",
                phone = "(12) 3921-1200",
                openingHours = "Segunda a Sexta: 08:30 às 17:30 (Atendimento Corporativo)",
                latitude = -23.1901,
                longitude = -45.8854
            )
        )

        fun findById(id: String): BjsegStore {
            return OFFICIAL_STORES.firstOrNull { it.id == id } ?: OFFICIAL_STORES.first()
        }
    }
}
