package com.example.kursachclient.utils

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*

object GooglePaymentUtils {

    private val CURRENCY_CODE = "RUB"
    private val TOKENIZATION_PUBLIC_KEY = "BCR2DN4T4S66RSKI"
    private val SUPPORTED_METHODS = arrayListOf(
        WalletConstants.PAYMENT_METHOD_CARD, )
//        WalletConstants.PAYMENT_METHOD_UNKNOWN,)
//        WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD,)
//        WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY,)
//        WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_DIRECT,)
//        WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_NETWORK_TOKEN)
    private val SUPPORTED_NETWORKS = arrayListOf(
        WalletConstants.CARD_NETWORK_AMEX,
        WalletConstants.CARD_NETWORK_DISCOVER,
        WalletConstants.CARD_NETWORK_INTERAC,
        WalletConstants.CARD_NETWORK_JCB,
        WalletConstants.CARD_NETWORK_MASTERCARD,
        WalletConstants.CARD_NETWORK_VISA)

    fun createGoogleApiClientForPay(context: Context): PaymentsClient =
        Wallet.getPaymentsClient(context,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build())
    fun checkIsReadyGooglePay(paymentsClient: PaymentsClient,
                              callback: (res: Boolean) -> Unit) {
        val isReadyRequest = IsReadyToPayRequest.newBuilder()
//            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_UNKNOWN)
//            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .build()
        val task = paymentsClient.isReadyToPay(isReadyRequest)
        task.addOnCompleteListener {
            try {
                if (it.getResult(ApiException::class.java))
                // можем показать кнопку оплаты, все хорошо
                    callback.invoke(true)
                else
                // должны спрятать кнопку оплаты
                    callback.invoke(false)
            } catch (e: ApiException) {
                e.printStackTrace()
                callback.invoke(false)
            }
        }
    }

    fun createPaymentDataRequest(price: String): PaymentDataRequest {
        val transaction = createTransaction(price)
        val request = generatePaymentRequest(transaction)
        return request
    }



    fun createTransaction(price: String): TransactionInfo =
        TransactionInfo.newBuilder()
            .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
            .setTotalPrice(price)
            .setCurrencyCode(CURRENCY_CODE)
            .build()

    private fun generatePaymentRequest(transactionInfo: TransactionInfo): PaymentDataRequest {
        val tokenParams = PaymentMethodTokenizationParameters
            .newBuilder()
            .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_CARD)
            .build()

        return PaymentDataRequest.newBuilder()
            .setPhoneNumberRequired(false)
            .setEmailRequired(false)
            .setShippingAddressRequired(false)
            .setTransactionInfo(transactionInfo)
            .addAllowedPaymentMethods(SUPPORTED_METHODS)
            .setCardRequirements(
                CardRequirements.newBuilder()
                .addAllowedCardNetworks(SUPPORTED_NETWORKS)
                .setAllowPrepaidCards(true)
                .setBillingAddressRequired(false)
                .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                .build())
            .setPaymentMethodTokenizationParameters(tokenParams)
            .setUiRequired(false)
            .build()
    }
}