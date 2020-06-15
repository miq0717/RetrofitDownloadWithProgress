package miq0717.downloadwithprogress

import android.os.Parcel
import android.os.Parcelable

/*Created by MiQ0717 on 15-Jun-2020.*/
class DownloadProgress : Parcelable {
    constructor()

    var progress = 0.0
    var currentFileSize = 0.0
    var totalFileSize = 0.0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(progress)
        dest.writeDouble(currentFileSize)
        dest.writeDouble(totalFileSize)
    }

    constructor(`in`: Parcel) {
        progress = `in`.readDouble()
        currentFileSize = `in`.readDouble()
        totalFileSize = `in`.readDouble()
    }

    companion object {
        val CREATOR: Parcelable.Creator<DownloadProgress?> = object : Parcelable.Creator<DownloadProgress?> {
            override fun createFromParcel(`in`: Parcel): DownloadProgress? {
                return DownloadProgress(`in`)
            }

            override fun newArray(size: Int): Array<DownloadProgress?> {
                return arrayOfNulls(size)
            }
        }
    }
}