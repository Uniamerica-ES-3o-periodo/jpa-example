package br.uniamerica;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    private static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernatejpa");

        Lembrete lembrete = new Lembrete();
        Lembrete lembretePorId = new Lembrete();
        lembrete.setTitulo("Comprar leite");
        lembrete.setDescricao("Hoje, 10h30");
        EntityManager em = entityManagerFactory.createEntityManager();

        List<Lembrete> lembretes = null;

        try {
            //insere no bd
            em.getTransaction().begin();
            System.out.println("======== INSERINDO.... =========");
            em.persist(lembrete);
            em.getTransaction().commit();
            System.out.println("======== INSERT OK =========");



            // busca 1 por id
            lembretePorId = em.find(Lembrete.class, 1L);
            if (lembretePorId != null) {
                System.out.println("======== BUSCA POR ID =========");
                System.out.println(lembretePorId.getTitulo() + " - " + lembretePorId.getDescricao());
            }

            //busca todos
            System.out.println("======== BUSCA TODOS =========");

            lembretes = em.createQuery("from Lembrete").getResultList();
            if (lembretes != null) {
                System.out.println("======== LISTA =========");

                for (Lembrete l:
                     lembretes) {
                    System.out.println(l.getTitulo());
                    System.out.println(l.getDescricao());
                }
            } else {
                System.out.println("======== não achou ninguem =========");
            }



            // busca por titulo
            lembretes = em.createQuery("from Lembrete l where l.titulo LIKE '%CHUCHU%'").getResultList();
            if (lembretes != null && lembretes.size() > 0) {
                System.out.println("======== LISTA POR TITULO =========");

                for (Lembrete l:
                        lembretes) {
                    System.out.println(l.getTitulo());
                    System.out.println(l.getDescricao());
                }
            } else {
                System.out.println("======== não achou ninguem =========");
            }


            // alterando por id
            System.out.println("======== BUSCANDO POR ID =========");
            lembrete = em.find(Lembrete.class, 1L);
            lembrete.setTitulo("Comprar café");
            lembrete.setDescricao("Hoje, 8h22");

            System.out.println("======== ALTERANDO POR ID =========");
            em.getTransaction().begin();
            em.merge(lembrete);
            em.getTransaction().commit();
            System.out.println("======== ALTERADO  =========");
            // busca 1 por id
            lembretePorId = em.find(Lembrete.class, 1L);
            if (lembretePorId != null) {
                System.out.println("======== BUSCA POR ID =========");
                System.out.println(lembretePorId.getTitulo() + " - " + lembretePorId.getDescricao());
            }


            //REMOVER POR ID
            System.out.println("======== BUSCANDO POR ID =========");
            em.getTransaction().begin();
            lembrete = em.find(Lembrete.class, 1L);
            System.out.println("======== REMOVENDO POR ID =========");
            em.remove(lembrete);
            em.getTransaction().commit();
            System.out.println("======== REMOVIDO  =========");
            // busca 1 por id
            lembretePorId = em.find(Lembrete.class, 1L);
            if (lembretePorId != null) {
                System.out.println("======== BUSCA POR ID =========");
                System.out.println(lembretePorId.getTitulo() + " - " + lembretePorId.getDescricao());
            } else{
                System.out.println("Registro com ID 1 não encontrado");
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("INSERT: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
