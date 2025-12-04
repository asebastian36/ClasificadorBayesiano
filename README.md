# Documentación del Proyecto: Clasificador Bayesiano de Hongos

## 1. Archivo: `Hongo.java`
**Rol Principal:** Es el **contenedor de información**.

Esta clase no realiza cálculos. Su única función es representar una fila del archivo de datos en la memoria del programa. Funciona como una "ficha" que agrupa la etiqueta (si es venenoso o comestible) junto con sus 22 características físicas, permitiendo que el resto del programa mueva estos datos como un solo paquete ordenado.

---

## 2. Archivo: `CargadorDatos.java`
**Rol Principal:** Es el **traductor de archivos**.

Se encarga de leer el archivo de texto (`.data`) y convertirlo en objetos `Hongo` que Java pueda entender.

* **Funcionamiento:**
    1.  Abre el archivo y lee línea por línea.
    2.  Separa cada línea usando la coma (`,`) como cortador.
    3.  Toma la primera letra como la **Clase** (la respuesta correcta).
    4.  Toma el resto de las letras como los **Atributos**.
    5.  Crea un nuevo objeto `Hongo` y lo guarda en una lista.

---

## 3. Archivo: `ClasificadorBayesiano.java`
**Rol Principal:** Es el **cerebro matemático**. Aquí reside la lógica de aprendizaje y predicción.

### Método: `entrenar(List<Hongo> datos)`
Este método construye la "memoria" del clasificador basándose en ejemplos pasados.

* **Lógica detallada:**
    1.  Recibe una lista de hongos (el 70% de los datos).
    2.  Recorre hongo por hongo.
    3.  Si el hongo es **Comestible**:
        * Suma 1 al contador total de comestibles.
        * Revisa sus 22 características una por una y anota en una tabla: *"He visto la letra 'x' en la columna 1 siendo comestible una vez más"*.
    4.  Si el hongo es **Venenoso**:
        * Hace lo mismo, pero en la tabla de venenosos.
    5.  Al final, tiene mapas completos de frecuencias (cuántas veces aparece cada característica para cada clase).

### Método: `clasificar(String[] atributos, boolean mostrarDetalle)`
Este método aplica la **Fórmula de Bayes** para decidir qué es un hongo nuevo.

* **Lógica detallada:**
    1.  **Probabilidad Inicial (Prior):** Calcula qué tan probable es que un hongo cualquiera sea venenoso basándose solo en la cantidad total que vio al entrenar (ej. 48% vs 52%).
    2.  **Multiplicación de Evidencias (Likelihood):**
        * Toma la primera característica del hongo nuevo.
        * Busca en sus tablas: *"¿Qué tan común es esta característica en los hongos venenosos?"*.
        * Multiplica esa probabilidad por la probabilidad acumulada.
        * Repite esto para las 22 características.
    3.  **Manejo de Ceros (Suavizado):** Si se encuentra una característica que nunca había visto antes (probabilidad 0), la sustituye por un número muy pequeño (0.0001) para evitar que la multiplicación total se vuelva cero y anule todo el cálculo.
    4.  **Comparación:** Al final tiene dos números (puntajes de probabilidad). Si el puntaje de Venenoso es mayor al de Comestible, clasifica el hongo como **"p"**, de lo contrario como **"e"**.
    5.  **Modo Detalle:** Si activas el modo detalle (`true`), imprime en pantalla cada multiplicación paso a paso.

---

## 4. Archivo: `Evaluador.java`
**Rol Principal:** Es el **auditor de calidad**. Mide qué tan confiable es el Clasificador.

### Método: `evaluarModelo(Clasificador, List<Hongo>)`
Realiza un simulacro de examen para calificar al algoritmo.

* **Lógica detallada:**
    1.  Toma una lista de hongos que el clasificador *no conoce* (el 30% restante).
    2.  Para cada hongo, le oculta la respuesta real y le pide al Clasificador que adivine.
    3.  Compara la adivinanza con la realidad y clasifica el resultado en cuatro grupos:
        * **Acierto Venenoso (VP):** Era veneno y el sistema alertó correctamente.
        * **Acierto Comestible (VN):** Era seguro y el sistema dijo que era seguro.
        * **Falsa Alarma (FP):** Era seguro, pero el sistema se equivocó y dijo venenoso.
        * **Error Peligroso (FN):** Era venenoso, pero el sistema dijo que era seguro (Falso Negativo).
    4.  Finalmente, usa estas cuatro cifras para calcular los porcentajes de rendimiento:
        * **Exactitud:** Porcentaje total de aciertos.
        * **Precisión:** De todas las alertas de veneno, ¿cuántas eran reales?
        * **Sensibilidad:** De todo el veneno que había, ¿cuánto fue capaz de detectar?

---

## 5. Archivo: `Main.java`
**Rol Principal:** Es el **coordinador del flujo**.

### Método: `main`
Controla el orden en que suceden las cosas.

* **Lógica detallada:**
    1.  Llama al `CargadorDatos` para leer el archivo del disco.
    2.  **Mezcla aleatoria (`shuffle`):** Desordena la lista de hongos. Esto es crucial para que el entrenamiento sea justo y no aprenda solo de un tipo de hongo si el archivo venía ordenado.
    3.  **Partición (Split):** Corta la lista en dos pedazos:
        * Los primeros 70% se envían al Clasificador para que "estudie" (`entrenar`).
        * El 30% restante se reserva en una caja separada para el examen final (`test`).
    4.  Inicia el **Menú Interactivo** que permite al usuario elegir entre usar el clasificador manualmente (Opción 1) o correr la evaluación automática (Opción 2).