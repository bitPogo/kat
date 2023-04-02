/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.awesomecats.store.data.RepositoryContract
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    ClientContract.ConnectivityManager::class,
    ClientContract.Logger::class,
)
class ClientFactorySpec {
    @Test
    @JsName("fn0")
    fun `It fulfils ClientFactory`() {
        CatClient fulfils RepositoryContract.ClientFactory::class
    }

    @Test
    @JsName("fn1")
    fun `Given getInstance is called it creates a CatClient`() {
        CatClient.getInstance(
            kmock(),
            kmock(),
        ) fulfils RepositoryContract.Client::class
    }
}
