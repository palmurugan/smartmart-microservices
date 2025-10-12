CREATE TYPE "attribute_datatype" AS ENUM (
  'string',
  'number',
  'boolean'
);

CREATE TABLE "category" (
  "id" uuid PRIMARY KEY,
  "org_id" uuid NOT NULL,
  "parent_id" uuid,
  "name" text NOT NULL,
  "description" varchar,
  "metadata" jsonb,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz,
  "is_active" boolean NOT NULL
);

CREATE TABLE "product" (
  "id" uuid PRIMARY KEY,
  "org_id" uuid NOT NULL,
  "category_id" uuid NOT NULL,
  "attributes" jsonb,
  "metadata" jsonb,
  "featured" boolean,
  "version" bigint,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz,
  "is_active" boolean NOT NULL
);

CREATE TABLE "product_localization" (
  "product_id" uuid NOT NULL,
  "locale" varchar NOT NULL,
  "name" text NOT NULL,
  "short_description" text,
  "long_description" text,
  "seo" jsonb,
  "search_vector" tsvector,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz,
  "is_active" boolean NOT NULL,
  "primary" key(product_id,locale)
);

CREATE TABLE "sku" (
  "id" uuid PRIMARY KEY,
  "org_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "sku_code" text NOT NULL,
  "barcode" text,
  "attributes" jsonb,
  "weight" numeric(12,4),
  "dimensions" jsonb,
  "version" bigint,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz,
  "is_active" boolean NOT NULL
);

CREATE TABLE "media" (
  "id" uuid PRIMARY KEY,
  "org_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "sku_id" uuid NOT NULL,
  "url" text NOT NULL,
  "type" text,
  "alt" jsonb,
  "position" int,
  "metadata" jsonb,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz,
  "is_active" boolean NOT NULL
);

CREATE TABLE "product_audit" (
  "id" bigserial PRIMARY KEY,
  "org_id" uuid,
  "entity_type" text,
  "entity_id" uuid,
  "action" text,
  "payload" jsonb,
  "performed_by" uuid,
  "performed_at" timestamptz
);

CREATE TABLE "attribute_definition" (
  "id" uuid PRIMARY KEY,
  "org_id" uuid,
  "category_id" uuid,
  "key" varchar NOT NULL,
  "data_type" attribute_datatype NOT NULL,
  "label" jsonb,
  "options" jsonb,
  "facetable" boolean,
  "searchable" boolean,
  "required" boolean,
  "sort_order" int,
  "created_at" timestamptz,
  "created_by" varchar,
  "updated_at" timestamptz,
  "updated_by" varchar
);

-- COMMENT ON COLUMN "category"."id" IS 'Primary key';
-- COMMENT ON COLUMN "category"."parent_id" IS 'Self reference';
-- COMMENT ON TABLE "product_localization" IS 'Multilingual product details';

ALTER TABLE "category" ADD FOREIGN KEY ("parent_id") REFERENCES "category" ("id");

ALTER TABLE "product" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");

ALTER TABLE "product_localization" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "sku" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "media" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "media" ADD FOREIGN KEY ("sku_id") REFERENCES "sku" ("id");

ALTER TABLE "attribute_definition" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");
