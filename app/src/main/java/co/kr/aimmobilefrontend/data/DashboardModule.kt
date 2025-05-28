package co.kr.aimmobilefrontend.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * dashboard 에 출력하기 위한 데이터셋.
 * 추후 api 를 통해 받아오도록 수정함.
 */
@Module
@InstallIn(SingletonComponent::class)
object AssetModule {

    @Provides
    fun provideHardcodedAssetItems(): List<AssetItem> {
        return listOf(
            AssetItem("TEST1", "stock", 1, 10.05f, "test security 1", "테스트를 위한 종목1"),
            AssetItem("TEST2", "stock", 1, 8.03f, "test security 2", "테스트를 위한 종목2"),
            AssetItem("TEST3", "stock", 1, 6.5f, "test security 3", "테스트를 위한 종목3"),
            AssetItem("TEST4", "stock", 1, 8.5f, "test security 4", "테스트를 위한 종목4"),
            AssetItem("TEST5", "bond", 3, 9.5f, "test security 5", "테스트를 위한 종목5"),
            AssetItem("TEST6", "bond", 1, 8.5f, "test security 6", "테스트를 위한 종목6"),
            AssetItem("TEST7", "bond", 1, 13.42f, "test security 7", "테스트를 위한 종목7"),
            AssetItem("usd_cash", "etc", 1, 35.5f, "cash", "현금")
        )
    }
}