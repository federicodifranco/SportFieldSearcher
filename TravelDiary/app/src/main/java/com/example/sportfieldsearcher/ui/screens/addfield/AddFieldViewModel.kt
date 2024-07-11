package com.example.sportfieldsearcher.ui.screens.addfield

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.sportfieldsearcher.data.database.entities.CategoryType
import com.example.sportfieldsearcher.data.database.entities.Field
import com.example.sportfieldsearcher.data.database.entities.PrivacyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddFieldState(
    val name: String = "",
    val date: String = "",
    val description: String = "",
    val category: CategoryType,
    val privacyType: PrivacyType,
    val city: String = "",
    val fieldPicture: Uri = Uri.EMPTY
) {
    val canSubmit
        get() = name.isNotBlank()
                && date.isNotBlank()
                && category != CategoryType.NONE
                && privacyType != PrivacyType.NONE
                && city.isNotBlank()

    fun toField(fieldAddedId: Int) = Field(
        name = name,
        date = date,
        description = description,
        category = category,
        privacyType = privacyType,
        fieldAddedId = fieldAddedId,
        city = city,
        fieldPicture = fieldPicture.toString()
    )
}

interface AddFieldActions {
    fun setName(name: String)
    fun setDate(date: String)
    fun setDescription(description: String)
    fun setCategory(category: CategoryType)
    fun setPrivacyType(privacyType: PrivacyType)
    fun setCity(city: String)
    fun setFieldPicture(imageUri: Uri)
}

class AddFieldViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddFieldState(category = CategoryType.NONE, privacyType = PrivacyType.NONE))
    val state = _state.asStateFlow()

    val actions = object : AddFieldActions {
        override fun setName(name: String) =
            _state.update { it.copy(name = name) }

        override fun setDate(date: String) =
            _state.update { it.copy(date = date) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setCategory(category: CategoryType) =
            _state.update { it.copy(category = category) }

        override fun setPrivacyType(privacyType: PrivacyType) =
            _state.update { it.copy(privacyType = privacyType) }

        override fun setCity(city: String) =
            _state.update { it.copy(city = city) }

        override fun setFieldPicture(imageUri: Uri) =
            _state.update { it.copy(fieldPicture = imageUri) }
    }
}
