CREATE TABLE public.t_owner(

id bigint NOT null,
first_name varchar(255),
last_name varchar(255)
);

CREATE TABLE public.t_pet(

id bigint NOT null,
name varchar(255),
birth_date date,
owner_id bigint,

);

ALTER TABLE public.t_owner ADD CONSTRAINT public.CONSTRAINT_1 PRIMARY key(id);
ALTER TABLE public.t_pet ADD CONSTRAINT public.CONSTRAINT_2 PRIMARY key(id);
ALTER TABLE public.t_pet ADD CONSTRAINT public.constraint_3 FOREIGN key(owner_id) referances public.t_owner(id) ;
CREATE sequance public.petclinic_sequence START WITH 100;
