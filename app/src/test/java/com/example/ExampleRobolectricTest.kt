package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BjsegRepository
import com.example.model.RmaStatus
import com.example.model.UserAccount
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `verify app name resource`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BJSEG Pós-Vendas", appName)
  }

  @Test
  fun `verify document validation`() {
    // Valid CPF & CNPJ
    assertTrue(UserAccount.isValidDocument("12.345.678/0001-90"))
    assertTrue(UserAccount.isValidDocument("12345678000190"))
    // Invalid documents
    assertFalse(UserAccount.isValidDocument("11111111111"))
    assertFalse(UserAccount.isValidDocument("123"))
  }

  @Test
  fun `verify 30 days deadline calculation`() {
    val rmas = BjsegRepository.INITIAL_RMAS
    val rma = rmas.first { it.id == "rma_01" } // opened 2026-10-18
    assertNotNull(rma.estimatedDeadlineDisplay)
    // 2026-10-18 + 30 days = 2026-11-17
    assertEquals("17/11/2026", rma.estimatedDeadlineDisplay)
  }

  @Test
  fun `verify rating awards one cent and prevents duplication`() {
    val repo = BjsegRepository.getInstance()
    // Test on rma_02 which is DISPONIVEL_RETIRADA and not yet rated
    val initialRma = BjsegRepository.INITIAL_RMAS.first { it.id == "rma_02" }
    assertFalse(initialRma.isRated)

    // Submit rating with 1 star (should still award R$ 0.01 per prompt specification!)
    val result = repo.submitRmaRating("rma_02", stars = 1, comment = "Demorou mas resolveu")
    assertTrue(result.isSuccess)
    assertEquals(0.01, result.getOrNull()!!, 0.0001)

    // Attempt second rating on same RMA (must fail with anti-duplication rule)
    val secondResult = repo.submitRmaRating("rma_02", stars = 5, comment = "Tentando duplicar")
    assertTrue(secondResult.isFailure)
  }

  @Test
  fun `verify whatsapp messages include rma number`() {
    val repo = BjsegRepository.getInstance()
    val paymentUrl = repo.buildWhatsAppUrl("RMA-2026-0842", isPayment = true)
    val supportUrl = repo.buildWhatsAppUrl("RMA-2026-0842", isPayment = false)

    assertTrue(paymentUrl.contains("RMA-2026-0842"))
    assertTrue(paymentUrl.contains("pagamento"))
    assertTrue(supportUrl.contains("RMA-2026-0842"))
  }
}
