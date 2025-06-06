package com.example.tastymood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Database(
    entities = [ Receta::class, Favorito::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recetaDao(): RecetaDao
    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recetas_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Poblar la base de datos con datos iniciales
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        populateDatabase(database)
                    }
                }
            }
        }

        private suspend fun populateDatabase(database: AppDatabase) {
            val recetaDao = database.recetaDao()

            // Insertar recetas de ejemplo
            val recetas = listOf(
                // Feliz Sin Dieta
                Receta(
                    nombreReceta = "Tostadas de aguacate",
                    ingredientes = "2 rebanadas de pan integral." +
                            "1 aguacate maduro." +
                            "½ limón." +
                            "Sal y pimienta al gusto." +
                            "1 cucharada de semillas de chía." +
                            "1 huevo pochado (opcional)",
                    preparacion = "Machaca el aguacate y mézclalo con el jugo de limón, sal y pimienta." +
                            "Unta la mezcla sobre las tostadas." +
                            "Espolvorea semillas de chía y coloca el huevo pochado encima." +
                            "¡Disfruta de cada bocado con felicidad!",
                    imagen = "https://i.pinimg.com/736x/c9/43/40/c943405935b134b00273dda9c7ffcd9b.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Sándwich de pollo y aguacate",
                    ingredientes = "2 rebanadas de pan integral." +
                            "1 pechuga de pollo cocida y desmenuzada." +
                            "½ aguacate." +
                            "1 cucharada de mayonesa." +
                            "La lechuga y el tomate al gusto." +
                            "Algo de sal y pimienta.",
                    preparacion = "Mezcla el pollo con la mayonesa, aguacate, sal y pimienta." +
                            "Coloca la mezcla entre dos rebanadas de pan junto con la lechuga y el tomate." +
                            "Disfruta un almuerzo saludable y delicioso.",
                    imagen = "https://i.pinimg.com/736x/5d/e9/84/5de98485c76753d366fe688ca133b3e9.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Panqueques de avena",
                    ingredientes = "1 taza de harina de avena." +
                            "1 huevo." +
                            "½ taza de leche." +
                            "1 cucharadita de polvo para hornear." +
                            "1 cucharada de miel." +
                            "1 cucharadita de canela." +
                            "Frutas al gusto.",
                    preparacion = "Mezcla todos los ingredientes hasta obtener una mezcla homogénea." +
                            "Vierte porciones en una sartén caliente y cocina hasta dorar ambos lados." +
                            "Sirve con frutas y disfruta.",
                    imagen = "https://i.pinimg.com/736x/bb/23/a8/bb23a891a210c78ba7d176353f541aae.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Tarta de manzana",
                    ingredientes = "2 tazas de manzana en rodajas." +
                            "½ taza de azúcar morena." +
                            "1 cucharadita de canela." +
                            "1 masa para tarta." +
                            "1 cucharada de mantequilla.",
                    preparacion = "Coloca las manzanas en la masa para tarta." +
                            "Espolvorea con azúcar y canela, y añade trozos de mantequilla." +
                            "Hornea a 180°C por 40 minutos y disfruta.",
                    imagen = "https://i.pinimg.com/736x/96/31/6b/96316b571c1dd8c3fc79295ca331027f.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Cupcakes de fresa",
                    ingredientes = "1 taza de harina." +
                            "½ taza de azúcar." +
                            "½ taza de mantequilla." +
                            "1 huevo." +
                            "½ taza de puré de fresas.",
                    preparacion = "Mezcla todos los ingredientes hasta obtener una masa homogénea." +
                            "Hornea a 180°C por 20 minutos y deja enfriar antes de decorar.",
                    imagen = "https://i.pinimg.com/736x/d6/e7/85/d6e78500cd0796d42a9744a28f10ec96.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                // Feliz en Vegetales
                Receta(
                    nombreReceta = "Tacos de champiñones",
                    ingredientes = "6 tortillas de maíz." +
                            "2 tazas de champiñones en rebanadas." +
                            "1 cebolla morada." +
                            "1 cucharada de aceite de oliva." +
                            "La sal y la pimienta al gusto." +
                            "El cilantro y el limón para servir.",
                    preparacion = "Sofríe los champiñones y la cebolla en aceite de oliva." +
                            "Añade sal y pimienta al gusto." +
                            "Sirve en tortillas de maíz y acompaña con cilantro y limón.",
                    imagen = "https://i.pinimg.com/736x/e2/e5/da/e2e5da7a046a83037a663908ae6d72ae.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Sopa de calabaza",
                    ingredientes = "1 calabaza grande." +
                            "1 zanahoria." +
                            "1 cebolla." +
                            "2 tazas de caldo de verduras." +
                            "La sal y la pimienta al gusto." +
                            "1 cucharada de aceite de oliva.",
                    preparacion = "Corta la calabaza, zanahoria y cebolla en trozos." +
                            "Sofríe la cebolla en aceite de oliva." +
                            "Añade la calabaza y zanahoria junto con el caldo de verduras." +
                            "Cocina hasta que estén suaves y licúa hasta obtener una crema homogénea.",
                    imagen = "https://i.pinimg.com/736x/f0/52/ec/f052ec92eec77c4ab5d974f64f424750.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Pasta con pesto de espinaca",
                    ingredientes = "200 g de pasta integral." +
                            "1 taza de espinaca fresca." +
                            "¼ de taza de nueces." +
                            "¼ de taza de queso parmesano." +
                            "2 cucharadas de aceite de oliva." +
                            "1 diente de ajo." +
                            "La sal y pimienta al gusto.",
                    preparacion = "Cuece la pasta según las instrucciones." +
                            "Licúa las hojas de espinaca, nueces, queso parmesano, aceite de oliva y ajo hasta obtener un pesto homogéneo." +
                            "Mezcla el pesto con la pasta y disfruta.",
                    imagen = "https://i.pinimg.com/736x/5d/a0/4e/5da04e97dcae39d2a1df3c358bc375bd.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Feliz"
                ),

                // Feliz en Animales
                Receta(
                    nombreReceta = "Huevos revueltos con tocino",
                    ingredientes = "2 huevos." +
                            "3 tiras de tocino." +
                            "1 cucharada de mantequilla." +
                            "La sal y pimienta al gusto.",
                    preparacion = "Cocina el tocino hasta que esté crujiente." +
                            "Bate los huevos con sal y pimienta y cocínalos en mantequilla." +
                            "Sirve con el tocino y disfruta.",
                    imagen = "https://i.pinimg.com/736x/dd/fb/e8/ddfbe880eb835bc6923b7717653c04fe.jpg",
                    tipoDieta = "En animales",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Hamburguesa de carne",
                    ingredientes = "200 g de carne molida de res." +
                            "1 huevo." +
                            "La sal y pimienta al gusto." +
                            "1 cucharada de aceite de oliva." +
                            "Pan de hamburguesa.",
                    preparacion = "Mezcla la carne con el huevo, sal y pimienta." +
                            "Forma una hamburguesa y cocina en aceite de oliva." +
                            "Sirve en pan con tus ingredientes favoritos.",
                    imagen = "https://i.pinimg.com/736x/09/94/c8/0994c8dc442a96fdc96bffe76aaca047.jpg",
                    tipoDieta = "En animales",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Pollo asado con especias",
                    ingredientes = "1 pechuga de pollo." +
                            "1 cucharada de aceite de oliva." +
                            "1 cucharadita de paprika." +
                            "1 cucharadita de ajo en polvo." +
                            "La sal y pimienta al gusto.",
                    preparacion = "Marina el pollo con aceite de oliva, paprika, ajo en polvo, sal y pimienta." +
                            "Hornea a 200°C por 25-30 minutos." +
                            "Sirve con una guarnición de tu elección.",
                    imagen = "https://i.pinimg.com/736x/53/07/eb/5307eb132b227b2699c2aeea86aacb61.jpg",
                    tipoDieta = "En animales",
                    emocion = "Feliz"
                ),

                // Feliz sin azucares
                Receta(
                    nombreReceta = "Galletas de almendra sin azúcar",
                    ingredientes = "1 taza de harina de almendra." +
                            "½ cucharadita de polvo para hornear." +
                            "3 cucharadas de cacao en polvo." +
                            "4 cucharadas de eritritol." +
                            "1 huevo." +
                            "4 cucharadas de mantequilla." +
                            "¼ taza de chispas de chocolate sin azúcar.",
                    preparacion = "Mezcla todos los ingredientes hasta formar una masa homogénea." +
                            "Forma las galletas y hornea a 180°C por 15 minutos.",
                    imagen = "https://i.pinimg.com/736x/5b/68/e2/5b68e2666ada4d21adce027d3273ca68.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Pastel de zanahoria sin azúcar",
                    ingredientes = "2 tazas de harina de almendra." +
                            "1 taza de zanahoria rallada." +
                            "½ taza de yogur griego sin azúcar." +
                            "1 cucharadita de canela." +
                            "1 cucharadita de polvo para hornear." +
                            "Endulzante al gusto.",
                    preparacion = "Mezcla todos los ingredientes hasta obtener una masa homogénea." +
                            "Hornea a 180°C por 30 minutos y deja enfriar antes de servir.",
                    imagen = "https://i.pinimg.com/736x/ed/10/45/ed1045bb3e580e4f3d9ea91c2cb2358c.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Flan de coco sin azúcar",
                    ingredientes = "1 taza de leche de coco." +
                            "2 huevos." +
                            "1 cucharadita de esencia de vainilla." +
                            "Endulzante al gusto.",
                    preparacion = "Mezcla todos los ingredientes y vierte en moldes individuales." +
                            "Hornea a baño maría a 180°C por 40 minutos." +
                            "Deja enfriar y disfruta.",
                    imagen = "https://i.pinimg.com/736x/44/49/d2/4449d2f4340c7e166510dc2f837039f9.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Feliz"                ),

                // Triste Sin Dieta
                Receta(
                    nombreReceta = "Sopa de fideos casera",
                    ingredientes = "200 g de fideos finos. " +
                            "1 litro de caldo de pollo. " +
                            "1 zanahoria cortada en cubitos. " +
                            "1 tallo de apio picado. " +
                            "1 cucharada de aceite de oliva. " +
                            "Sal y pimienta al gusto.",
                    preparacion = "Calienta el aceite en una olla y sofríe la zanahoria y el apio. " +
                            "Añade el caldo de pollo y deja hervir. " +
                            "Agrega los fideos y cocina hasta que estén tiernos. " +
                            "Sazona con sal y pimienta.",
                    imagen = "https://i.pinimg.com/736x/e1/61/fe/e161fe0b528a159336a121cd6bd60c06.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Puré de papas cremoso",
                    ingredientes = "4 papas grandes. " +
                            "½ taza de leche tibia. " +
                            "2 cucharadas de mantequilla. " +
                            "Sal al gusto. " +
                            "Un poco de queso rallado (opcional).",
                    preparacion = "Hierve las papas hasta que estén suaves. " +
                            "Escurre y machaca con la leche y mantequilla. " +
                            "Sazona con sal y espolvorea queso si deseas.",
                    imagen = "https://i.pinimg.com/736x/1c/f2/61/1cf261d87b4afba35ab4748dbf561d0f.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Té de manzanilla con miel",
                    ingredientes = "1 bolsita de té de manzanilla. " +
                            "1 taza de agua caliente. " +
                            "1 cucharada de miel. " +
                            "Un poco de limón (opcional).",
                    preparacion = "Prepara el té de manzanilla en agua caliente. " +
                            "Añade la miel y unas gotas de limón. " +
                            "Disfruta mientras está caliente para relajarte.",
                    imagen = "https://i.pinimg.com/736x/c1/f7/89/c1f789f34ed230954913a784e7b79b4d.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Pastel de chocolate clásico",
                    ingredientes = "2 tazas de harina." +
                            "1 taza de azúcar." +
                            "½ taza de cacao en polvo." +
                            "1 cucharadita de polvo para hornear." +
                            "1 taza de leche." +
                            "½ taza de mantequilla derretida." +
                            "2 huevos.",
                    preparacion = "Mezcla los ingredientes secos en un tazón." +
                            "Añade la leche, mantequilla y huevos, y bate hasta obtener una mezcla homogénea." +
                            "Hornea a 180°C por 30 minutos y deja enfriar antes de servir.",
                    imagen = "https://i.pinimg.com/736x/53/5b/c8/535bc87b01054ed51d8f2a830b4e5deb.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),
                Receta(
                    nombreReceta = "Brownies de chocolate",
                    ingredientes = "1 taza de azúcar." +
                            "½ taza de mantequilla derretida." +
                            "½ taza de cacao en polvo." +
                            "2 huevos." +
                            "1 taza de harina." +
                            "½ cucharadita de polvo para hornear.",
                    preparacion = "Mezcla todos los ingredientes hasta obtener una masa homogénea." +
                            "Hornea a 180°C por 25 minutos y deja enfriar antes de cortar.",
                    imagen = "https://i.pinimg.com/736x/8e/21/51/8e2151b89370805d3360d56019f4888d.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Feliz"
                ),

                // Triste en Vegetales
                Receta(
                    nombreReceta = "Crema de brócoli reconfortante",
                    ingredientes = "2 tazas de brócoli. " +
                            "1 papa mediana. " +
                            "1 cebolla pequeña. " +
                            "2 tazas de caldo de verduras. " +
                            "2 cucharadas de aceite de oliva. " +
                            "Sal y pimienta al gusto.",
                    preparacion = "Sofríe la cebolla en aceite hasta que esté dorada. " +
                            "Añade el brócoli, papa y caldo de verduras. " +
                            "Cocina hasta que las verduras estén suaves y licúa. " +
                            "Sazona con sal y pimienta.",
                    imagen = "https://i.pinimg.com/736x/27/33/21/2733216f9d2d82823b0a6df4b779959c.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Ensalada de lentejas tibia",
                    ingredientes = "1 taza de lentejas cocidas. " +
                            "1 zanahoria cocida en cubitos. " +
                            "½ cebolla morada finamente picada. " +
                            "2 cucharadas de aceite de oliva. " +
                            "1 cucharada de vinagre balsámico. " +
                            "Perejil fresco picado.",
                    preparacion = "Mezcla las lentejas tibias con la zanahoria y cebolla. " +
                            "Aliña con aceite de oliva y vinagre balsámico. " +
                            "Espolvorea perejil fresco y sirve tibia.",
                    imagen = "https://i.pinimg.com/736x/9d/f4/eb/9df4eb9febecfd1405234bec5ff4a9f3.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Arroz con verduras al vapor",
                    ingredientes = "1 taza de arroz integral. " +
                            "2 tazas de caldo de verduras. " +
                            "1 calabacín en cubitos. " +
                            "1 pimiento rojo en tiras. " +
                            "1 cucharada de aceite de sésamo. " +
                            "Sal al gusto.",
                    preparacion = "Cocina el arroz en el caldo de verduras. " +
                            "Mientras tanto, cuece al vapor las verduras. " +
                            "Mezcla el arroz con las verduras y aceite de sésamo.",
                    imagen = "https://i.pinimg.com/736x/4c/48/50/4c48500b84d1af2dafa40dcf8ce52b3d.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Triste"
                ),

                // Triste en Animales
                Receta(
                    nombreReceta = "Pollo a la plancha simple",
                    ingredientes = "1 pechuga de pollo. " +
                            "1 cucharada de aceite de oliva. " +
                            "Sal y pimienta al gusto. " +
                            "Jugo de medio limón. " +
                            "Hierbas finas (tomillo o romero).",
                    preparacion = "Sazona el pollo con sal, pimienta y hierbas. " +
                            "Cocina a la plancha con aceite de oliva por 6-8 minutos por lado. " +
                            "Rocía con jugo de limón antes de servir.",
                    imagen = "https://i.pinimg.com/736x/6b/56/2c/6b562c1c119651d2e449890bda56e9c8.jpg",
                    tipoDieta = "En animales",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Salmón al horno con limón",
                    ingredientes = "1 filete de salmón. " +
                            "1 limón en rodajas. " +
                            "1 cucharada de aceite de oliva. " +
                            "Sal y pimienta al gusto. " +
                            "Eneldo fresco.",
                    preparacion = "Coloca el salmón en papel aluminio con rodajas de limón. " +
                            "Rocía con aceite de oliva, sal, pimienta y eneldo. " +
                            "Hornea a 200°C por 15-20 minutos.",
                    imagen = "https://i.pinimg.com/736x/df/4c/64/df4c640f03c5fa6221090ebd49858058.jpg",
                    tipoDieta = "En animales",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Caldo de res reconfortante",
                    ingredientes = "300 g de carne de res para caldo. " +
                            "1 hueso de res. " +
                            "1 zanahoria. " +
                            "1 cebolla. " +
                            "1 tallo de apio. " +
                            "Sal y pimienta al gusto.",
                    preparacion = "Hierve la carne y el hueso en agua por 2 horas. " +
                            "Añade las verduras y cocina 30 minutos más. " +
                            "Cuela el caldo y sazona con sal y pimienta.",
                    imagen = "https://i.pinimg.com/736x/c8/17/a5/c817a56ec8cdfeb35591f09176880ca4.jpg",
                    tipoDieta = "En animales",
                    emocion = "Triste"
                ),

                // Triste sin azúcares
                Receta(
                    nombreReceta = "Avena caliente sin azúcar",
                    ingredientes = "½ taza de avena. " +
                            "1 taza de leche de almendras. " +
                            "1 cucharadita de canela. " +
                            "1 cucharada de semillas de chía. " +
                            "Stevia al gusto. " +
                            "Nueces picadas.",
                    preparacion = "Cocina la avena en leche de almendras hasta que esté cremosa. " +
                            "Añade canela, stevia y semillas de chía. " +
                            "Sirve caliente con nueces encima.",
                    imagen = "https://i.pinimg.com/736x/0f/a2/7d/0fa27da88e1eff1f5db05085f633e980.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Yogur griego con nueces",
                    ingredientes = "1 taza de yogur griego natural. " +
                            "¼ taza de nueces picadas. " +
                            "1 cucharadita de extracto de vainilla. " +
                            "Stevia al gusto. " +
                            "Canela en polvo.",
                    preparacion = "Mezcla el yogur con extracto de vainilla y stevia. " +
                            "Añade las nueces picadas y espolvorea canela. " +
                            "Sirve frío como un postre reconfortante.",
                    imagen = "https://i.pinimg.com/736x/49/73/2d/49732dbd44b6f9cdc866f7e24896d5b6.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Triste"
                ),
                Receta(
                    nombreReceta = "Infusión de jengibre y limón",
                    ingredientes = "1 trozo de jengibre fresco. " +
                            "Jugo de medio limón. " +
                            "1 taza de agua caliente. " +
                            "Stevia al gusto. " +
                            "Hojas de menta (opcional).",
                    preparacion = "Hierve el agua con el jengibre por 5 minutos. " +
                            "Añade el jugo de limón y stevia. " +
                            "Sirve caliente, añade menta si deseas.",
                    imagen = "https://i.pinimg.com/736x/0e/6e/b1/0e6eb107c2daec1db1eeda605ee9c7f5.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Triste"
                ),

                // Enojado Sin Dieta
                Receta(
                    nombreReceta = "Pasta arrabiata picante",
                    ingredientes = "200 g de pasta. " +
                            "3 tomates grandes. " +
                            "3 dientes de ajo. " +
                            "1-2 chiles rojos secos. " +
                            "3 cucharadas de aceite de oliva. " +
                            "Sal al gusto. " +
                            "Perejil fresco.",
                    preparacion = "Sofríe el ajo y chiles en aceite hasta que estén dorados. " +
                            "Añade los tomates triturados y cocina 15 minutos. " +
                            "Mezcla con la pasta cocida y espolvorea perejil.",
                    imagen = "https://i.pinimg.com/736x/ce/de/ed/cedeed1535137fb7df671dcfae54303c.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Quesadillas de queso picantes",
                    ingredientes = "2 tortillas de harina. " +
                            "1 taza de queso rallado. " +
                            "1 chile jalapeño en rodajas. " +
                            "1 cucharada de aceite. " +
                            "Salsa picante al gusto.",
                    preparacion = "Coloca queso y jalapeños entre las tortillas. " +
                            "Cocina en sartén con aceite hasta que esté dorado. " +
                            "Sirve con salsa picante.",
                    imagen = "https://i.pinimg.com/736x/e4/48/5a/e4485a9a0ada67c317195b94c45c1cb2.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Sopa de tomate picante",
                    ingredientes = "4 tomates grandes. " +
                            "1 cebolla. " +
                            "2 dientes de ajo. " +
                            "1 cucharadita de paprika picante. " +
                            "2 tazas de caldo de verduras. " +
                            "Aceite de oliva.",
                    preparacion = "Sofríe cebolla y ajo en aceite. " +
                            "Añade tomates y paprika, cocina 10 minutos. " +
                            "Agrega el caldo, hierve y licúa hasta obtener una crema.",
                    imagen = "https://i.pinimg.com/736x/b9/a6/4e/b9a64e691613f342f36b253ba81503a4.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Brownies de chocolate picantes",
                    ingredientes = "1 taza de azúcar. " +
                            "½ taza de mantequilla derretida. " +
                            "½ taza de cacao en polvo. " +
                            "2 huevos. " +
                            "1 taza de harina. " +
                            "½ cucharadita de chile en polvo.",
                    preparacion = "Mezcla todos los ingredientes hasta obtener una masa homogénea. " +
                            "Hornea a 180°C por 25 minutos y deja enfriar antes de cortar.",
                    imagen = "https://i.pinimg.com/736x/f5/76/5e/f5765e637841c00c6248e2e2bda75115.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Tarta de manzana picante",
                    ingredientes = "2 manzanas grandes. " +
                            "1 cucharada de canela. " +
                            "1 cucharadita de chile en polvo. " +
                            "1 masa para tarta. " +
                            "½ taza de azúcar moreno.",
                    preparacion = "Precalienta el horno a 180°C. " +
                            "Corta las manzanas en rodajas y mezcla con canela, chile y azúcar. " +
                            "Rellena la masa con la mezcla y hornea por 30 minutos.",
                    imagen = "https://i.pinimg.com/736x/c5/82/b8/c582b8af6f74161ae8a4a02e6076c51d.jpg",
                    tipoDieta = "Ninguna",
                    emocion = "Enojado"
                ),

                // Enojado en Vegetales
                Receta(
                    nombreReceta = "Curry de garbanzos picante",
                    ingredientes = "1 taza de garbanzos cocidos. " +
                            "1 cebolla grande. " +
                            "2 tomates. " +
                            "1 cucharada de curry en polvo. " +
                            "1 cucharadita de chile en polvo. " +
                            "Aceite de coco. " +
                            "Cilantro fresco.",
                    preparacion = "Sofríe la cebolla hasta que esté dorada. " +
                            "Añade tomates, curry y chile en polvo. " +
                            "Agrega los garbanzos y cocina 15 minutos. " +
                            "Decora con cilantro fresco.",
                    imagen = "https://i.pinimg.com/736x/5f/1f/6f/5f1f6fe5b15f7293431eff566584ccf4.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Stir fry de verduras con chile",
                    ingredientes = "1 pimiento rojo. " +
                            "1 pimiento verde. " +
                            "1 cebolla. " +
                            "2 dientes de ajo. " +
                            "1 chile serrano. " +
                            "2 cucharadas de salsa de soya. " +
                            "Aceite de sésamo.",
                    preparacion = "Calienta aceite en wok o sartén grande. " +
                            "Saltea todas las verduras a fuego alto por 5 minutos. " +
                            "Añade salsa de soya y chile, cocina 2 minutos más.",
                    imagen = "https://i.pinimg.com/736x/4a/94/8b/4a948bb87f62a5ed1d4e709eccb428d8.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Tacos de frijoles con jalapeños",
                    ingredientes = "1 taza de frijoles refritos. " +
                            "4 tortillas de maíz. " +
                            "2 jalapeños en rodajas. " +
                            "1 cebolla morada. " +
                            "Cilantro fresco. " +
                            "Salsa verde picante.",
                    preparacion = "Calienta los frijoles refritos. " +
                            "Rellena las tortillas con frijoles, jalapeños y cebolla. " +
                            "Añade cilantro y salsa verde al gusto.",
                    imagen = "https://i.pinimg.com/736x/38/2a/ef/382aeffcf80716976df59e7b6c59daf6.jpg",
                    tipoDieta = "En vegetales",
                    emocion = "Enojado"
                ),

                // Enojado en Animales
                Receta(
                    nombreReceta = "Alitas picantes al horno",
                    ingredientes = "8 alitas de pollo. " +
                            "2 cucharadas de salsa búfalo. " +
                            "1 cucharada de aceite de oliva. " +
                            "1 cucharadita de paprika. " +
                            "½ cucharadita de chile en polvo. " +
                            "Sal al gusto.",
                    preparacion = "Mezcla las alitas con todos los condimentos. " +
                            "Marina por 30 minutos. " +
                            "Hornea a 200°C por 25-30 minutos hasta que estén doradas.",
                    imagen = "https://i.pinimg.com/736x/c0/f0/f7/c0f0f7b181fe1cf8faaae61c6bb77e92.jpg",
                    tipoDieta = "En animales",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Carne asada con chile chipotle",
                    ingredientes = "300 g de carne para asar. " +
                            "2 chiles chipotle en adobo. " +
                            "1 cucharada de aceite. " +
                            "Sal y pimienta al gusto. " +
                            "Jugo de limón.",
                    preparacion = "Marina la carne con chiles chipotle machacados y aceite. " +
                            "Asa a la parrilla por 4-5 minutos por lado. " +
                            "Rocía con jugo de limón antes de servir.",
                    imagen = "https://i.pinimg.com/736x/7d/1b/0d/7d1b0da763400438bf7cbf233040636b.jpg",
                    tipoDieta = "En animales",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Pescado a la diabla",
                    ingredientes = "2 filetes de pescado blanco. " +
                            "2 chiles guajillo. " +
                            "1 tomate. " +
                            "2 dientes de ajo. " +
                            "Aceite para freír. " +
                            "Sal al gusto.",
                    preparacion = "Licúa los chiles hidratados con tomate y ajo. " +
                            "Fríe los filetes de pescado. " +
                            "Cubre con la salsa de chile y cocina 5 minutos más.",
                    imagen = "https://i.pinimg.com/736x/c9/48/6d/c9486db23e2f7b19e034239162979be8.jpg",
                    tipoDieta = "En animales",
                    emocion = "Enojado"
                ),

                // Enojado sin azúcares
                Receta(
                    nombreReceta = "Ensalada picante sin azúcar",
                    ingredientes = "2 tazas de lechuga mixta. " +
                            "1 pepino en rodajas. " +
                            "1 chile serrano picado. " +
                            "2 cucharadas de aceite de oliva. " +
                            "1 cucharada de vinagre de manzana. " +
                            "Sal y pimienta negra.",
                    preparacion = "Mezcla todas las verduras en un tazón. " +
                            "Aliña con aceite, vinagre, sal y pimienta. " +
                            "Añade chile serrano al gusto para dar un toque picante.",
                    imagen = "https://i.pinimg.com/736x/11/4b/31/114b311d6b13d1d3e28223fcb64dcaa0.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Té verde con jengibre picante",
                    ingredientes = "1 bolsita de té verde. " +
                            "1 trozo de jengibre fresco. " +
                            "1 pizca de pimienta cayena. " +
                            "1 taza de agua caliente. " +
                            "Stevia al gusto.",
                    preparacion = "Prepara el té verde con agua caliente. " +
                            "Añade jengibre rallado y una pizca de cayena. " +
                            "Endulza con stevia y disfruta caliente.",
                    imagen = "https://i.pinimg.com/736x/b8/e4/32/b8e432d51148b42bfa29101dc11fd557.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Enojado"
                ),
                Receta(
                    nombreReceta = "Aguacate con chile piquín",
                    ingredientes = "1 aguacate maduro. " +
                            "Jugo de medio limón. " +
                            "½ cucharadita de chile piquín. " +
                            "Sal al gusto. " +
                            "Semillas de girasol.",
                    preparacion = "Corta el aguacate en rebanadas. " +
                            "Rocía con jugo de limón y espolvorea chile piquín. " +
                            "Añade sal y semillas de girasol.",
                    imagen = "https://i.pinimg.com/736x/08/2c/ea/082cea05b0253146ded978be5a0e2a26.jpg",
                    tipoDieta = "Sin azúcares",
                    emocion = "Enojado"
                )
            )

            recetas.forEach { receta ->
                recetaDao.insertReceta(receta)
            }
        }
    }
}

// Converters (si necesitas convertir tipos de datos)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}