Contexto: Proyecto Maven multimódulo en Java 21, arrancando desde cero.
Módulos existentes: payroll-shared, payroll-core, payroll-persistence,
payroll-api. Necesito SOLO las clases de entidad JPA (@Entity) del módulo
payroll-persistence, más los enums de dominio que aún no existen en
payroll-shared (nada de repositorios, mappers, Flyway ni controladores
todavía).

PASO 1 — En el módulo payroll-core, crea estos 4 enums:

- RiskLevel (nivel de riesgo ARL colombiano), con 5 constantes y una
  tarifa asociada (campo `rate` de tipo BigDecimal):
    ONE(0.00522), TWO(0.01044), THREE(0.02436), FOUR(0.0435), FIVE(0.0696)
  Cada constante debe tener también un `label` descriptivo (ej. "Riesgo I
  — Mínimo"). Constructor privado, getters vía Lombok o manuales.

- OvertimeType (horas extra y recargos según el CST colombiano), con
  campos `surcharge` (BigDecimal, porcentaje de recargo) e `isOvertime`
  (boolean: true si es hora extra propiamente dicha, false si es solo
  recargo sobre hora ordinaria):
    EXTRA_DIURNA(0.25, true)
    EXTRA_NOCTURNA(0.75, true)
    EXTRA_DOMINICAL_FESTIVA_DIURNA(1.00, true)
    EXTRA_DOMINICAL_FESTIVA_NOCTURNA(1.50, true)
    RECARGO_NOCTURNO(0.35, false)
    RECARGO_DOMINICAL_FESTIVO(0.75, false)
    RECARGO_DOMINICAL_FESTIVO_HABITUAL(1.00, false)
  Cada constante con un `label` legible.

- NaturalezaConcepto: DEVENGO, DEDUCCION, APORTE_PATRONAL, PROVISION

- TipoNovedad: INCAPACIDAD, LICENCIA_MATERNIDAD, LICENCIA_PATERNIDAD,
  VACACIONES, LICENCIA_NO_REMUNERADA

PASO 2 — En el módulo payroll-persistence, paquete
com.portfolio.payroll.persistence.entity, crea las siguientes entidades
JPA, usando los enums del PASO 1 (payroll-persistence debe declarar
dependencia hacia payroll-shared en su pom.xml si aún no existe):

- Empresa (id UUID PK, nit, razonSocial, exoneradoParafiscales boolean,
  estado boolean)

- Empleado (id Long PK, empresa -> @ManyToOne a Empresa, tipoDocumento,
  numeroDocumento, nombres, apellidos, tipoTrabajador)

- Contrato (id Long PK, empresa -> @ManyToOne a Empresa, empleado ->
  @ManyToOne a Empleado, tipoContrato, salarioBase BigDecimal,
  esSalarioIntegral boolean, nivelRiesgoArl -> RiskLevel del PASO 1,
  fechaInicio LocalDate, fechaFin LocalDate nullable)

- Nomina (id Long PK, empresa -> @ManyToOne a Empresa, contrato ->
  @ManyToOne a Contrato, periodoInicio LocalDate, periodoFin LocalDate,
  diasTrabajados int, totalDevengado, totalDeducido, netoPagar,
  totalCostoEmpleador -> todos BigDecimal, fechaLiquidacion Instant)

- RegistroTiempo (id Long PK, nomina -> @ManyToOne a Nomina, tipoHora ->
  OvertimeType del PASO 1, cantidadHoras BigDecimal, porcentajeRecargo
  BigDecimal, valorTotal BigDecimal)

- ConceptoPago (id Long PK, empresa -> @ManyToOne a Empresa NULLABLE
  [null = concepto global del sistema], codigo, nombre, naturaleza ->
  NaturalezaConcepto del PASO 1)

- DetalleNomina (id Long PK, nomina -> @ManyToOne a Nomina, concepto ->
  @ManyToOne a ConceptoPago, valorCalculado BigDecimal, referenciaSoporte
  String nullable)

- Novedad (id Long PK, empleado -> @ManyToOne a Empleado, tipoNovedad ->
  TipoNovedad del PASO 1, fechaInicio LocalDate, fechaFin LocalDate,
  diasAplicados int)

- ParametroSistema (id Long PK, anio int, nombre String, valor BigDecimal)

Requisitos técnicos:
1. Lombok en todas las entidades: @Getter, @Setter, @Builder,
   @NoArgsConstructor, @AllArgsConstructor.
2. Todos los valores monetarios como BigDecimal con
   @Column(precision = 15, scale = 2).
3. Empresa usa @Id @GeneratedValue(strategy = GenerationType.UUID); el
   resto usa @Id @GeneratedValue(strategy = GenerationType.IDENTITY).
4. Todas las relaciones @ManyToOne con fetch = FetchType.LAZY y
   @JoinColumn explícito en snake_case (ej. empleado_id). La relación
   ConceptoPago.empresa debe ser opcional (nullable = true).
5. Agrega @CreatedDate y @LastModifiedDate (createdAt, updatedAt tipo
   Instant) en cada entidad, con
   @EntityListeners(AuditingEntityListener.class). No generes la clase
   donde se habilita @EnableJpaAuditing, eso va después.
6. Verifica que el pom.xml de payroll-persistence tenga las dependencias
   spring-boot-starter-data-jpa y lombok; si el proyecto aún no tiene
   parent POM con Spring Boot configurado, indícamelo en vez de asumirlo.
7. Compatible con Java 21 (record no aplica aquí porque las entidades
   necesitan mutabilidad y constructor vacío para JPA).
8. No generes repositorios, DTOs, servicios, mappers ni migraciones —
   solo los 4 enums del PASO 1 y las 8 entidades del PASO 2.