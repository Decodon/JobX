package ie.wit.jobx.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class JobModel(var id: Long = 0,
                    var title: String = "",
                    var description: String = "",
                    var date: String = "",
                    var image: Uri = Uri.EMPTY,
                    var lat : Double = 0.00,
                    var lng: Double = 0.00,
                    var net: Double = 0.00,
                    var vat: Double = 0.00,
                    var gross: Double = 0.00,
                    var zoom: Float = 0f) : Parcelable

/**
 * Location data class
 */
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable