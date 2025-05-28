package co.kr.aimmobilefrontend.data


data class AssetItem(
    val security_symbol: String,
    val type: String, // stock, bond, etc
    val quantity: Int,
    val ratio: Float,
    val security_name: String,
    val security_description: String? = null,

    )
