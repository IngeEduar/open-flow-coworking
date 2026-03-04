CREATE TABLE public.access_log (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    check_in timestamp(6) with time zone,
    check_out timestamp(6) with time zone,
    price numeric(38,2),
    status character varying(255) NOT NULL,
    branch_id uuid NOT NULL,
    client_id uuid NOT NULL,
    operator_id uuid NOT NULL,
    CONSTRAINT access_log_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'COMPLETED'::character varying])::text[])))
);

CREATE TABLE public.backoffice_user (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    document character varying(20) NOT NULL,
    email character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    salt character varying(255) NOT NULL,
    CONSTRAINT backoffice_user_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'OPERATOR'::character varying])::text[])))
);

CREATE TABLE public.branch (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    address character varying(100) NOT NULL,
    hourly_rate numeric(38,2) NOT NULL,
    max_capacity integer NOT NULL,
    name character varying(50) NOT NULL
);

CREATE TABLE public.branch_operator (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    branch_id uuid NOT NULL,
    backoffice_user_id uuid NOT NULL
);

CREATE TABLE public.client (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    document character varying(20) NOT NULL,
    email character varying(50) NOT NULL
);

CREATE TABLE public.coupons (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    recycle boolean DEFAULT false NOT NULL,
    updated_at timestamp(6) with time zone,
    code character varying(100) NOT NULL,
    expired_at timestamp(6) with time zone,
    status character varying(255) NOT NULL,
    branch_id uuid NOT NULL,
    client_id uuid NOT NULL,
    CONSTRAINT coupons_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'EXPIRED'::character varying, 'USED'::character varying])::text[])))
);

ALTER TABLE ONLY public.access_log
    ADD CONSTRAINT access_log_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.backoffice_user
    ADD CONSTRAINT backoffice_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.branch_operator
    ADD CONSTRAINT branch_operator_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT coupons_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.client
    ADD CONSTRAINT uk90ans9v7nohqmaorc4wg4tmvv UNIQUE (document);

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT ukeplt0kkm9yf2of2lnx6c1oy9b UNIQUE (code);

ALTER TABLE ONLY public.backoffice_user
    ADD CONSTRAINT ukkm0yppcm1p31utrp0ofji17p3 UNIQUE (document);

ALTER TABLE ONLY public.backoffice_user
    ADD CONSTRAINT ukkognhgnn51dkma6qdtpb9gv2d UNIQUE (email);

ALTER TABLE ONLY public.branch_operator
    ADD CONSTRAINT fk21w81g5l8e82955ulf6vaukt8 FOREIGN KEY (backoffice_user_id) REFERENCES public.backoffice_user(id);

ALTER TABLE ONLY public.access_log
    ADD CONSTRAINT fk6nmn9m5aucmeydu18wwuhkc4w FOREIGN KEY (branch_id) REFERENCES public.branch(id);

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT fk9ohmkhcgwad5rx56bwgjptakf FOREIGN KEY (client_id) REFERENCES public.client(id);

ALTER TABLE ONLY public.access_log
    ADD CONSTRAINT fkc5qigbb1ojylgbbbbjsfhqag FOREIGN KEY (operator_id) REFERENCES public.backoffice_user(id);

ALTER TABLE ONLY public.branch_operator
    ADD CONSTRAINT fkgy5c1cnwm1j83ksx6x2nqg25t FOREIGN KEY (branch_id) REFERENCES public.branch(id);

ALTER TABLE ONLY public.access_log
    ADD CONSTRAINT fkkvqb5ji1v9mv7qjkdrww6f8xj FOREIGN KEY (client_id) REFERENCES public.client(id);

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT fkpdx263ndycg5aj81ke4ivhbuj FOREIGN KEY (branch_id) REFERENCES public.branch(id);
