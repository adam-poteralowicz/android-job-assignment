package com.schibsted.nde.testUtils

import com.schibsted.nde.model.MealResponse

object MealFactory {

    fun meal(
        id: String = "0",
        category: String = "category",
        name: String = "name",
        image: String = "image",
        instructions: String = "instructions"
    ) = MealResponse(id, category, name, image, instructions)
}