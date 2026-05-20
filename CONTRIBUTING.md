# **Cómo contribuir**

Para este proyecto, en su mayoría se tratan de seguir las convenciones del lenguaje Java en la
medida de lo posible. Algunos casos quedan aquí explícitos, así como también excepciones a los
mismos acordadas en el equipo.

<u>Dichas convenciones deberían seguirse y pueden ser motivo de
rechazar un *Pull Request*.</u>

<!--suppress HtmlDeprecatedAttribute -->
<hr width="30%" align="left" />

# Índice

- [Código Fuente](#código-fuente)

- [*Pull Requests*](#pull-requests)
    * [Motivación](#motivación)
    * [Cambios Hechos](#cambios-hechos)
    * [Lista de cambios](#lista-de-cambios)
    * [*Checklist*](#checklist)

- [*Commits*](#commits)

- [*Issues*](#issues)

<hr width="30%" align="left" />

# Código Fuente

En lo posible, se utilizará la sintaxis que permita el _linter_ preferido por el equipo.

<hr/>

# *Pull Requests*

Los *pull requests* o "PRs" deben seguir el estilo de la
[plantilla](./doc/templates/pull_requests/pr_template.md) diseñada para tal fin. <br/>
En ella, se encuentran las siguientes secciones:

## Motivación

Acá debería ir una descripción breve del PR y el propósito que pretende cumplir.

## Cambios Hechos

Una descripción breve de los cambios hechos, y cómo podrían afectar desde ahora al código con el
que interactúa.

## Lista de cambios

Una lista (si no es exhaustiva, que comprenda los puntos más importantes), detallando los cambios
precisos hechos en el código u en otro lugar.

* Normalmente
* tendrá un
* formato
    - más o
    - menos
        * parecido
* a esto.

## *Checklist*

He aquí una casilla de casos más comunes para llenar en todo modelo de PRs. En caso de querer
agregar más para el PR en cuestión se puede, pero no quitar alguna de las que ya vienen en la
plantilla. <br/>

**Nota:** *El estilo que aparece en GitHub parece exclusivo de esta página y no funciona solo con MarkDown,
así que a continuación se detalla la forma de crear y editar algunas.*

```md
#### Casillas vacías:

*(válidas)*
* [ ]
- [ ]

*(inválidas)*
* []
- []
* [asd]

#### Casillas "llenas":

* [x]
* [X]
- [x]
- [X]
```

**Todos** los *pull requests* deberían ir acompañados de un "asignado" _(assignee)_, etiquetas, y
el proyecto correspondiente cuando se lo publica en GitHub. Opcionalmente, también debería estar
asociado a un *issue* y *milestone*.

<hr/>

# *Commits*

Los títulos de los *commits* deben de ser precedidos por `"<categoria>: "` de la manera:
```
<categoria>: Título del commit
```
Donde `<categoria>` refiere a uno de los siguientes casos:

* **feat:** Una nueva *feature*.
* **fix:** Un arreglo de un *bug*.
* **docs:** Cambios en la documentación.
* **style:** Cambios que no afectan al código de manera funcional.
* **refactor:** Cambios que no arreglan errores o agregan *features*.
* **test:** Cambios que agregan tests.
* **chore:** Cambios hechos a programas auxiliares del proyecto, como la compilación automática
  del programa.

Si no se identifica la ocasión con uno de estos casos, se puede evitar el prefijo.

**No es obligatorio** que los *commits* tengan una descripción.

<hr/>

# *Issues*

Las *issues* deberán seguir una plantilla según el [caso](./doc/templates/issues/) que convenga.
De no estar contemplado el caso en una plantilla, se puede seguir un
estilo libre (pero se espera uno similar). <br/>

Los casos en cuestión son:

* 🐛 [Reportar un error](./doc/templates/issues/bug_report_template.md)
* 🎨 [Una idea de diseño](./doc/templates/issues/design_idea_template.md)
* 📚 [Una mejora de la documentación](./doc/templates/issues/docs_augmentation_template.md)
* 🚀 [Una idea de *feature*](./doc/templates/issues/feature_request_template.md)
* 🚧 [Una ocasión en la que refactorizar código](./doc/templates/issues/refactor_code_template.md)

Donde el título del *issue* **debe empezar sí o sí** con el emoji correspondiente a esa categoría.
De no entrar en ninguna, el *issue* de estilo "libre" puede incluir cualquier emoji que no sea
uno de esos. <br/>

*En lo posible,* tratar de encajar la necesidad en alguna de esas categorías. **Por ejemplo:** un
reporte de una vulnerabilidad de seguridad podría ir acompañada de una refactorización, entonces
caería en la categoría 🚧; también, agregar librerías o extensiones para compilar el juego u otras
operaciones externas bien podrían ser 📚 o 🚀.
